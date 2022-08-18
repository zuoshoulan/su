package com.cpp.supplychain.gateway.router.filter.factory;

import com.cpp.supplychain.bss.commons.constants.ScmIntranetConstants;
import com.cpp.supplychain.bss.commons.exception.CustomException;
import com.cpp.supplychain.bss.tool.utils.encrypt.Rsa256KeyUtil;
import com.cpp.supplychain.gateway.router.errorCode.RouterErrorCode;
import com.cpp.supplychain.gateway.router.r2dbc.UrlWhiteListEntity;
import com.cpp.supplychain.gateway.router.task.UrlWhiteListTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author zhengweikang@hz-cpp.com
 * @date 2022/6/20 20:05
 */
@Slf4j
@Component
public class AppAuthGatewayFilterFactory extends AbstractGatewayFilterFactory implements AuthGatewayFilterFactory {

    private Rsa256KeyUtil.Rsa256Key rsa256Key = getRsa256Key();

    @Override
    public GatewayFilter apply(Object config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                String traceId = String.valueOf(exchange.getAttributes().get(ScmIntranetConstants.scmTraceIdRoamKey));
                log.info("AppAuthGatewayFilterFactory filter start traceId:{}", traceId);
                ServerHttpRequest request = exchange.getRequest();
                String innerAuthUrl = buildInnerAuthUrl(request.getURI().getRawPath());

                /**
                 * 解析jwt获取userId
                 */
                Long userId = getUserIdFromToken(exchange);
                Long finalUserId = userId;
                log.info("AppAuthGatewayFilterFactory finalUserId:{} traceId:{}", finalUserId, traceId);

                // 判断是否在白名单
                AntPathMatcher pathMatcher = new AntPathMatcher();
                List<UrlWhiteListEntity> whiteListEntities = UrlWhiteListTask.urlWhiteListEntityList;
                for (UrlWhiteListEntity whiteListEntity : whiteListEntities) {
                    if (BooleanUtils.isTrue(whiteListEntity.getIsEffective()) && pathMatcher.match(whiteListEntity.getUrl(), innerAuthUrl)) {
                        ServerHttpRequest request0 = exchange.getRequest().mutate()
                                .headers(httpHeaders -> httpHeaders.add(scUserIdKey, String.valueOf(finalUserId))).build();
                        return chain.filter(exchange.mutate().request(request0).build());
                    }
                }

                String token = getToken(exchange);
                if (StringUtils.isEmpty(token)) {
                    throw new CustomException(RouterErrorCode.ERROR_TOKEN_NOT_EXIST);
                }
                verifyToken(token, rsa256Key);

                ServerHttpRequest request0 = exchange.getRequest().mutate()
                        .headers(httpHeaders -> httpHeaders.add(scUserIdKey, String.valueOf(finalUserId))).build();
                return chain.filter(exchange.mutate().request(request0).build());
            }
        };
    }
}
