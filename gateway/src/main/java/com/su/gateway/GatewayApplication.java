package com.su.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @RequestMapping(value = "/actuator/hi", method = RequestMethod.GET)
    public Object hi() {
        return "hi";
    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        String uri = "https://www.baidu.com/s?wd=";
        String uri = "https://www.baidu.com";
        return builder.routes()
                .route("baidu", predicateSpec -> predicateSpec
                        .predicate(serverWebExchange -> {
                            ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();
                            RequestPath path = serverWebExchange.getRequest().getPath();
                            String sss = path.pathWithinApplication().value();
                            if (Objects.equals("/favicon.ico", sss)) {
                                return false;
                            }
                            return true;
                        }).and().method(HttpMethod.GET)
                        .uri(uri))
//                .route("myrule", fn -> fn
//                        .method(HttpMethod.POST)
//                        .and()
//                        .predicate(serverWebExchange -> {
//                            return true;
//                        })
//                        .filters(new Function<GatewayFilterSpec, UriSpec>() {
//                            @Override
//                            public UriSpec apply(GatewayFilterSpec gatewayFilterSpec) {
//                                return gatewayFilterSpec.filter(new GatewayFilter() {
//                                    @Override
//                                    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//                                        return chain.filter(exchange);
//                                    }
//                                });
//                            }
//                        })
//                        .uri("http://localhost:9990").order(Ordered.HIGHEST_PRECEDENCE))

                .build();
    }

}
