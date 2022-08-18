package com.cpp.supplychain.gateway.router.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author zhengweikang@hz-cpp.com
 * @date 2022/6/21 12:52
 */
@Component
@Slf4j
public class PreFlightFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        HttpMethod method = request.getMethod();
        if (HttpMethod.OPTIONS == method) {
            serverHttpResponse.setStatusCode(HttpStatus.NO_CONTENT);
            return serverHttpResponse.setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -99;
    }
}
