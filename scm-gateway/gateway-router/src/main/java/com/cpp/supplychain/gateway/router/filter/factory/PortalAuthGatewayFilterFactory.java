package com.cpp.supplychain.gateway.router.filter.factory;

import com.alibaba.fastjson.JSONObject;
import com.cpp.supplychain.bss.commons.constants.ScmIntranetConstants;
import com.cpp.supplychain.bss.commons.exception.CustomException;
import com.cpp.supplychain.bss.commons.model.Result;
import com.cpp.supplychain.bss.tool.utils.encrypt.Rsa256KeyUtil;
import com.cpp.supplychain.bss.tool.utils.jwt.JwtTokenUtil;
import com.cpp.supplychain.gateway.router.errorCode.RouterErrorCode;
import com.cpp.supplychain.gateway.router.r2dbc.UrlWhiteListEntity;
import com.cpp.supplychain.gateway.router.task.UrlWhiteListTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author zhengweikang@hz-cpp.com
 * @date 2022/6/20 20:31
 */
@Slf4j
@Component
public class PortalAuthGatewayFilterFactory extends AbstractGatewayFilterFactory implements AuthGatewayFilterFactory {

    @Resource
    private LoadBalancerClientFactory clientFactory;

    private Rsa256KeyUtil.Rsa256Key rsa256Key = getRsa256Key();

    @Override
    public GatewayFilter apply(Object config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                String scmTraceId = String.valueOf(exchange.getAttributes().get(ScmIntranetConstants.scmTraceIdRoamKey));
                log.info("PortalAuthGatewayFilterFactory filter start traceId:{}", scmTraceId);
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse serverHttpResponse = exchange.getResponse();
                String innerAuthUrl = buildInnerAuthUrl(request.getURI().getRawPath());

                /**
                 * 解析jwt获取userId
                 */
                Long userId = getUserIdFromToken(exchange);
                Long finalUserId = userId;
                log.info("PortalAuthGatewayFilterFactory traceId:{} finalUserId:{}", scmTraceId, finalUserId);

                // 判断是否在白名单
                AntPathMatcher pathMatcher = new AntPathMatcher();
                List<UrlWhiteListEntity> whiteListEntities = UrlWhiteListTask.urlWhiteListEntityList;
                for (UrlWhiteListEntity whiteListEntity : whiteListEntities) {
                    if (BooleanUtils.isTrue(whiteListEntity.getIsEffective()) && pathMatcher.match(whiteListEntity.getUrl(), innerAuthUrl)) {
                        ServerHttpRequest request0 = exchange.getRequest().mutate().header(scUserIdKey, String.valueOf(finalUserId)).build();
                        return chain.filter(exchange.mutate().request(request0).build());
                    }
                }
                String token = getToken(exchange);
                if (StringUtils.isEmpty(token)) {
                    throw new CustomException(RouterErrorCode.ERROR_TOKEN_NOT_EXIST);
                }
                verifyToken(token, rsa256Key);

                ReactorLoadBalancer<ServiceInstance> loadBalancer = clientFactory.getInstance(serviceId,
                        ReactorServiceInstanceLoadBalancer.class);
                URI url = null;
                try {
                    url = new URI(authUri);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                HttpHeaders headers = new HttpHeaders();
                RequestData requestData = new RequestData(HttpMethod.GET, url, headers, headers, new HashMap<>());

                DefaultRequest<RequestDataContext> lbRequest = new DefaultRequest<>(
                        new RequestDataContext(requestData, getHint(serviceId, clientFactory)));
                Mono<Response<ServiceInstance>> choose = loadBalancer.choose(lbRequest);

                Set<LoadBalancerLifecycle> supportedLifecycleProcessors = LoadBalancerLifecycleValidator
                        .getSupportedLifecycleProcessors(clientFactory.getInstances(serviceId, LoadBalancerLifecycle.class),
                                RequestDataContext.class, ResponseData.class, ServiceInstance.class);

                return choose.doOnNext(response -> {
                }).map(new Function<Response<ServiceInstance>, URI>() {
                    @Override
                    public URI apply(Response<ServiceInstance> response) {

                        if (!response.hasServer()) {
                            supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
                                    .onComplete(new CompletionContext<>(CompletionContext.Status.DISCARD, lbRequest, response)));
                            throw NotFoundException.create(false, "Unable to find instance for auth");
                        }
                        ServiceInstance retrievedInstance = response.getServer();
                        String overrideScheme = retrievedInstance.isSecure() ? "https" : "http";

                        DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(retrievedInstance,
                                overrideScheme);
                        URI uri = null;
                        try {
                            uri = new URI(authUri);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }

                        URI requestUrl = reconstructURI(serviceInstance, uri);
                        log.info("auth requestUrl:{} traceId:{}", requestUrl, scmTraceId);
                        return requestUrl;
                    }
                }).flatMap(new Function<URI, Mono<Void>>() {
                    @Override
                    public Mono<Void> apply(URI uri) {

                        ServerHttpRequest exchangeRequest = exchange.getRequest();
                        HttpMethod httpMethod = exchangeRequest.getMethod();

                        WebClient webClient = WebClient.create();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("method", httpMethod.name());
                        jsonObject.put("userId", finalUserId);
                        jsonObject.put("url", innerAuthUrl);

//                        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
//                        authenticationRequest.setMethod(httpMethod.name());
//                        authenticationRequest.setUserId(finalUserId);
//                        //path从设置上获取
//                        authenticationRequest.setUrl(innerAuthUrl);
//                        String bodyJson = JSONObject.toJSONString(authenticationRequest);

                        String bodyJson = jsonObject.toJSONString();
                        uri.getScheme();
                        Mono<Result> monoResponse = webClient
                                .post()
                                .uri(uri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(bodyJson))
                                .header(ScmIntranetConstants.scmTraceIdRoamKey, scmTraceId)
                                .retrieve()
                                .bodyToMono(Result.class);
                        return monoResponse
                                .flatMap(new Function<Result, Mono<Void>>() {
                                    @Override
                                    public Mono<Void> apply(Result result) {
                                        log.info("result:{} traceId:{}", result, scmTraceId);
                                        boolean auth = StringUtils.equals(successCode, result.getCode());
                                        log.info("uri:{} auth:{} traceId:{}", uri, auth, scmTraceId);
                                        if (auth) {
                                            //用RemoveRequestHeader filter把header中的sc-user-id先抹除
                                            ServerHttpRequest request = exchange.getRequest().mutate().header(scUserIdKey, String.valueOf(finalUserId)).build();
                                            return chain.filter(exchange.mutate().request(request).build())
                                                    //todo 这一行自测用
                                                    .then(Mono.fromRunnable(() -> exchange.getResponse().getHeaders().add(scUserIdKey, String.valueOf(finalUserId))))
                                                    ;
                                        }
                                        throw new CustomException(result.getCode(), result.getMessage());
                                    }
                                })
                                ;
                    }
                });
            }
        };
    }
}
