package com.cpp.supplychain.gateway.router.filter.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cpp.supplychain.bss.commons.constants.ScmIntranetConstants;
import com.cpp.supplychain.bss.commons.exception.CustomException;
import com.cpp.supplychain.bss.tool.utils.encrypt.Rsa256KeyUtil;
import com.cpp.supplychain.gateway.router.errorCode.RouterErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

/**
 * @author zhengweikang@hz-cpp.com
 * @date 2022/6/20 20:02
 */
public interface AuthGatewayFilterFactory {

    Logger log = LoggerFactory.getLogger(AuthGatewayFilterFactory.class);

    //strip后的path用于鉴权
    int stripPrefixPart = 1;

    //鉴权服务名
    String serviceId = "auth";

    //鉴权服务的path
    String authPath = "/auth/v1/auth";

    //用于 1.选出对应的服务instance 2.转化成内部url 3.host和port不重要，会替换掉
    String authUri = "http://localhost:10000" + authPath;

    //鉴权服务通过时code
    String successCode = "0000000";

    String scUserIdKey = "SC-User-Id";

    String SC_AUTHORIZATION_KEY = "Authorization";


    default URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }

    /**
     * http://host:port/api-app/(?<segment>.*)
     *
     * @param rawPath
     * @return segment
     */
    default String buildInnerAuthUrl(String rawPath) {
        String[] originalParts = org.springframework.util.StringUtils.tokenizeToStringArray(rawPath, "/");

        StringBuilder innerAuthUrl = new StringBuilder("/");
        for (int i = 0; i < originalParts.length; i++) {
            if (i >= stripPrefixPart) {
                // only append slash if this is the second part or greater
                if (innerAuthUrl.length() > 1) {
                    innerAuthUrl.append('/');
                }
                innerAuthUrl.append(originalParts[i]);
            }
        }
        if (innerAuthUrl.length() > 1 && rawPath.endsWith("/")) {
            innerAuthUrl.append('/');
        }
        return innerAuthUrl.toString();
    }

    /**
     * copy自ReactiveLoadBalancerClientFilter内部实现
     *
     * @param serviceId
     * @return
     */
    default String getHint(String serviceId, LoadBalancerClientFactory clientFactory) {
        LoadBalancerProperties loadBalancerProperties = clientFactory.getProperties(serviceId);
        Map<String, String> hints = loadBalancerProperties.getHint();
        String defaultHint = hints.getOrDefault("default", "default");
        String hintPropertyValue = hints.get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
    }

    default String[] splitToken(String token) throws JWTDecodeException {
        String[] parts = token.split("\\.");
        if (parts.length == 2 && token.endsWith(".")) {
            parts = new String[]{parts[0], parts[1], ""};
        }
        if (parts.length != 3) {
            throw new JWTDecodeException(String.format("The token was expected to have 3 parts, but got %s.", parts.length));
        } else {
            return parts;
        }
    }

    String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDbIMk+PQPfSMO/YC5B8MWzqY4B9jYWqU2d3464rR54KQwStZv6+Nipkyb08htbXoq6B6i4kJT+nb6NuKFN8h2fHvuvVleLqipXPmi1w0xQp8ll+Z7NRKspUWMpfA3fjgJAVmjkZHPWIG/esxyVwFc0sQxqhyTcu4bLHEuOnAoOvwIDAQAB";
    String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANsgyT49A99Iw79gLkHwxbOpjgH2NhapTZ3fjritHngpDBK1m/r42KmTJvTyG1teiroHqLiQlP6dvo24oU3yHZ8e+69WV4uqKlc+aLXDTFCnyWX5ns1EqylRYyl8Dd+OAkBWaORkc9Ygb96zHJXAVzSxDGqHJNy7hsscS46cCg6/AgMBAAECgYEAuldIKzARwegL5gtxasvII781Lr/kQSMaCP3aTXuSZJZrWOGtUiAZhyLGxxe3Ydyk1gu7U7BHs7oLGFOAgFs7CcmRDKG1GnKEccXeOxpVkJ1MWIdQQlH1pjyelbZzGuMik502mA9qIVzoZ0XEKBiwaz/2tj4hAEjpBMVsHLCRfpECQQDyBezJrbM4oYkLyZ7csjj4fGJrjyZcWkEKwjBGpqoEgFTn8bBbWbYCVxOawNFihzF1DCe9OBicVxvpfcZXT4EHAkEA58hhQqitkvwWSDFF3wrlKokHV26Djz5mNI3honeiSEvPOB+nlIu98M5a3brPyV3pGHv7KcRZw7SYDxqOtEtuiQJAbZwTVm+NmJtIBwekJA2vUZJ7vHzTgcX6aRXzaTv4ChvY2EcUePrTPJIVNeQXsEjMNFdx8/dg5gVphcDJwCcopwJASEhPPJCKAOqosD7dgV9OdGNjbLrEQGm/SehtU1NnwpARwXjts/+Ybn6MC9X0cuHcjYwsmavoKoYpXZx0oHldsQJBANqhLq8MDpdTgZuufvBxkvaLewbtxbwKpdi5S3uig5wAKkRBGFaDsbDxsWeHN3FzSy5oKHmFX0W1EkpB/ezMwmc=";

    default Rsa256KeyUtil.Rsa256Key getRsa256Key() {
        RSAPublicKey publicKey = Rsa256KeyUtil.stringToRsaPublicKey(PUBLIC_KEY);
        RSAPrivateKey privateKey = Rsa256KeyUtil.stringToRsaPrivateKey(PRIVATE_KEY);
        return new Rsa256KeyUtil.Rsa256Key(publicKey, privateKey);
    }

    default String getToken(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        List<String> authHeaders = headers.get(SC_AUTHORIZATION_KEY);
        if (!CollectionUtils.isEmpty(authHeaders)) {
            String token = authHeaders.get(0);
            return token;
        }
        return null;
    }

    default Long getUserIdFromToken(ServerWebExchange exchange) {
        String traceId = String.valueOf(exchange.getAttributes().get(ScmIntranetConstants.scmTraceIdRoamKey));
        try {
            String token = getToken(exchange);
            if (StringUtils.isNoneEmpty(token)) {
                String[] tokenParts = splitToken(token);
                byte[] result = org.apache.commons.codec.binary.Base64.decodeBase64(tokenParts[1].getBytes());
                String str = new String(result);
                JSONObject jsonObject = JSON.parseObject(str);
                Long userId = jsonObject.getLong("userId");
                return userId;
            }
        } catch (Exception e) {
            log.error("AuthGatewayFilterFactory traceId:{} getUserIdFromToken ignore error", traceId, e);
        }
        return null;
    }

    String JWT_ISSUE = "cpp-scm";

    default void verifyToken(String token, Rsa256KeyUtil.Rsa256Key rsa256Key) {
        Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
        JWTVerifier verifier = JWT
                .require(algorithm)
                .withIssuer(JWT_ISSUE)
                .build();
        try {
            verifier.verify(token);
        } catch (TokenExpiredException e) {
            throw new CustomException(RouterErrorCode.ERROR_TOKEN_EXP);
        } catch (JWTVerificationException e) {
            throw new CustomException(RouterErrorCode.ERROR_TOKEN_ILLEGAL);
        }
    }

}
