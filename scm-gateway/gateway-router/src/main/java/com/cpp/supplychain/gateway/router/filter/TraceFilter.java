package com.cpp.supplychain.gateway.router.filter;

import cn.hutool.core.lang.UUID;
import com.cpp.supplychain.bss.commons.constants.ScmIntranetConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author zhengweikang@hz-cpp.com
 * @Date 2022/6/23 23:08
 */
@Component
@Slf4j
public class TraceFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = UUID.fastUUID().toString();
        exchange.getAttributes().put(ScmIntranetConstants.scmTraceIdRoamKey, traceId);
        exchange.getResponse().getHeaders().add(ScmIntranetConstants.scmTraceIdRoamKey, traceId);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
