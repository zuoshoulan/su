package com.cpp.supplychain.gateway.router.filter;


import com.cpp.supplychain.bss.commons.constants.ScmIntranetConstants;
import com.cpp.supplychain.gateway.router.constants.ScmConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * 全局日志Filter
 *
 * @author yuanzhi.wang
 */
@Component
@Slf4j
public class GlobalLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        final OnceWrap onceWrap = new OnceWrap();

        String traceId = String.valueOf(exchange.getAttributes().get(ScmIntranetConstants.scmTraceIdRoamKey));
        onceWrap.setTraceId(traceId);
        exchange.getAttributes().put(ScmConstants.onceWrapKey, onceWrap);
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header(ScmIntranetConstants.scmTraceIdRoamKey, traceId)
                .build();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        HttpMethod method = request.getMethod();
        ServerHttpResponseDecorator response = getServerHttpResponseDecorator(exchange, startTime, onceWrap);


        onceWrap.setMethod(method.name());
        onceWrap.setRawPath(request.getURI().getRawPath());
        onceWrap.setQueryMap(queryParams);

        Class inClass = String.class;
        Class outClass = String.class;
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<?> modifiedBody = serverRequest.bodyToMono(inClass).flatMap(originalBody -> {
            onceWrap.setRequestBody(originalBody);
            return Mono.just(originalBody);
        });

        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() ->
                chain.filter(exchange.mutate().request(requestDecorate(exchange, headers, outputMessage)).response(response).build())
        )).onErrorResume((Function<Throwable, Mono<Void>>) throwable -> Mono.error(throwable));

//        return chain.filter(exchange.mutate().request(request).response(response).build());
    }

    private ServerHttpResponseDecorator getServerHttpResponseDecorator(ServerWebExchange exchange, long startTime, OnceWrap onceWrap) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator response = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                HttpStatus statusCode = getStatusCode();
                Long rt = System.currentTimeMillis() - startTime;
                onceWrap.setRt(rt);
                onceWrap.setResponseStatus(statusCode.value());
                log.info("GlobalLogFilter traceId:{} statusCode:{} rt:{}", onceWrap.getTraceId(), statusCode, rt);
                if (body instanceof Flux) {
                    // 获取ContentType，判断是否返回JSON格式数据
                    String originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    if (StringUtils.isNotBlank(originalResponseContentType) && originalResponseContentType.contains("application/json")) {
                        // 如果需要加密才进行处理
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);
                            DataBufferUtils.release(join);
                            String responseData = new String(content, Charset.forName("UTF-8"));
                            log.info("GlobalLogFilter traceId:{} responseData:{}", onceWrap.getTraceId(), responseData);
                            onceWrap.setResponseBody(responseData);
                            log.info("onceWrap:{}", onceWrap);
                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body)
                        .flatMapSequential(p -> p));
            }
        };
        return response;
    }


    private ServerHttpRequestDecorator requestDecorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(headers);
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }


    //打印response需要比NettyWriteResponseFilter order小
    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }

    @Data
    public static class OnceWrap {

        private String traceId;

        private Long rt;

        private String method;

        private String rawPath;

        private Map queryMap;

        private Object requestBody;

        private Integer responseStatus;

        private Object responseBody;


    }
}
