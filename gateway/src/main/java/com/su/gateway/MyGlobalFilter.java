package com.su.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.BiConsumer;

/**
 * @Author Weikang Lan
 * @Created 2019-03-01 17:53
 */
@Component("myGlobalFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class MyGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Long start = System.currentTimeMillis();
        log.info("start: {}", start);
        RequestPath path = exchange.getRequest().getPath();

        log.info("path={}", path.contextPath());

        return chain.filter(exchange).doOnSuccessOrError(new BiConsumer<Void, Throwable>() {
            @Override
            public void accept(Void aVoid, Throwable throwable) {
                log.info("start again: {}", start);
                Long end = System.currentTimeMillis();
                System.out.println("MyGlobalFilter: ");
                log.warn("MyGlobalFilter 耗时 {} ms", (end - start));
            }
        });
    }


}
