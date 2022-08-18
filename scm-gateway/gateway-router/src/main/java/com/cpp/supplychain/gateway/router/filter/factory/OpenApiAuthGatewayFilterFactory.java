package com.cpp.supplychain.gateway.router.filter.factory;

import com.alibaba.fastjson.JSONObject;
import com.cpp.supplychain.bss.commons.constants.ScmIntranetConstants;
import com.cpp.supplychain.bss.commons.exception.CustomException;
import com.cpp.supplychain.bss.commons.model.Result;
import com.cpp.supplychain.gateway.router.constants.ScmConstants;
import com.cpp.supplychain.gateway.router.filter.GlobalLogFilter;
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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author zhengweikang@hz-cpp.com
 * @Date 2022/8/1 09:38
 */
@Slf4j
@Component
public class OpenApiAuthGatewayFilterFactory extends AbstractGatewayFilterFactory {

    @Resource
    private LoadBalancerClientFactory clientFactory;

    //鉴权服务名
    String serviceId = "auth";
    //鉴权服务的path
    String authPath = "/auth/v1/openApiAuth";

    //用于 1.选出对应的服务instance 2.转化成内部url 3.host和port不重要，会替换掉
    String authUri = "http://localhost:10000" + authPath;

    String appInfoUri = "http://localhost:10000" + "/auth/v1/appInfo";

    //鉴权服务通过时code
    String successCode = "0000000";
    int stripPrefixPart = 1;

    @Override
    public GatewayFilter apply(Object config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                String scmTraceId = String.valueOf(exchange.getAttributes().get(ScmIntranetConstants.scmTraceIdRoamKey));
                log.info("OpenApiAuthGatewayFilterFactory filter start traceId:{}", scmTraceId);
                ServerHttpRequest request = exchange.getRequest();
                // 判断是否在白名单
                if (isInWhiteList(exchange)) {
                    return chain.filter(exchange);
                }

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
                        JSONObject param = new JSONObject();

                        GlobalLogFilter.OnceWrap onceWrap = (GlobalLogFilter.OnceWrap) exchange.getAttributes().get(ScmConstants.onceWrapKey);

                        String body = String.valueOf(onceWrap.getRequestBody());
                        JSONObject jsonObject = JSONObject.parseObject(body);
                        param.put("paramMap", jsonObject);

                        String bodyJson = param.toJSONString();
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
                                            ServerHttpRequest request = exchange.getRequest().mutate()
                                                    .headers(httpHeaders -> httpHeaders.add("appkey", "12345")).build();
                                            return chain.filter(exchange.mutate().request(request).build())
                                                    //todo 这一行自测用
                                                    .then(Mono.fromRunnable(() -> exchange.getResponse().getHeaders().add("appkey", "12345")))
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


    String getHint(String serviceId, LoadBalancerClientFactory clientFactory) {
        LoadBalancerProperties loadBalancerProperties = clientFactory.getProperties(serviceId);
        Map<String, String> hints = loadBalancerProperties.getHint();
        String defaultHint = hints.getOrDefault("default", "default");
        String hintPropertyValue = hints.get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
    }

    URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }

    String buildInnerAuthUrl(String rawPath) {
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


    private boolean isInWhiteList(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String innerAuthUrl = buildInnerAuthUrl(request.getURI().getRawPath());
        AntPathMatcher pathMatcher = new AntPathMatcher();
        List<UrlWhiteListEntity> whiteListEntities = UrlWhiteListTask.urlWhiteListEntityList;
        for (UrlWhiteListEntity whiteListEntity : whiteListEntities) {
            if (BooleanUtils.isTrue(whiteListEntity.getIsEffective()) && pathMatcher.match(whiteListEntity.getUrl(), innerAuthUrl)) {
                return true;
            }
        }
        return false;
    }
}
