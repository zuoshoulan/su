package com.su.gateway;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@EnableEurekaClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @RequestMapping(value = "/actuator/hi", method = RequestMethod.GET)
    public Object hi() {
        return "hi";
    }


//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
////        String uri = "https://www.baidu.com/s?wd=";  actuator
//        String uri = "https://www.baidu.com";
//        return builder.routes()
////                .route(new Function<PredicateSpec, Route.AsyncBuilder>() {
////                    @Override
////                    public Route.AsyncBuilder apply(PredicateSpec predicateSpec) {
////                        return predicateSpec.predicate(new Predicate<ServerWebExchange>() {
////                            @Override
////                            public boolean test(ServerWebExchange serverWebExchange) {
////                                return false;
////                            }
////                        }).uri("");
////                    }
////                })
//                .route("baidu", predicateSpec -> predicateSpec
//                        .predicate(serverWebExchange -> {
//                            ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();
//                            RequestPath path = serverWebExchange.getRequest().getPath();
//                            String sss = path.pathWithinApplication().value();
//                            if (Objects.equals("/favicon.ico", sss) || StringUtils.startsWith(sss, "/actuator")) {
//                                return false;
//                            }
//                            return true;
//                        }).and().method(HttpMethod.GET).and().host("localhost")
//                        .uri(uri))
////                .route("myrule", fn -> fn
////                        .method(HttpMethod.POST)
////                        .and()
////                        .predicate(serverWebExchange -> {
////                            return true;
////                        })
////                        .filters(new Function<GatewayFilterSpec, UriSpec>() {
////                            @Override
////                            public UriSpec apply(GatewayFilterSpec gatewayFilterSpec) {
////                                return gatewayFilterSpec.filter(new GatewayFilter() {
////                                    @Override
////                                    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
////                                        return chain.filter(exchange);
////                                    }
////                                });
////                            }
////                        })
////                        .uri("http://localhost:9990").order(Ordered.HIGHEST_PRECEDENCE))
//
//                .build();
//    }

}
