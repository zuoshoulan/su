package com.cpp.supplychain.gateway.router.advice;

import com.cpp.supplychain.bss.commons.exception.CustomException;
import com.cpp.supplychain.gateway.router.errorCode.RouterErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 全局异常处理
 *
 * @author yuanzhi.wang
 */
@Component
@Slf4j
public class GlobalAdviceController extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = super.getError(request);
        log.error("error", error);
        Map<String, Object> errorAttributes = new HashMap<>(8);
        MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations
                .from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
        HttpStatus errorStatus = determineHttpStatus(error, responseStatusAnnotation);
        errorAttributes.put("ts", new Date());
        //必须设置, 否则会报错, 因为 DefaultErrorWebExceptionHandler 的 renderErrorResponse 方法会获取此属性, 重新实现 DefaultErrorWebExceptionHandler也可.
        errorAttributes.put("status", errorStatus.value());
        errorAttributes.put("code", RouterErrorCode.ERROR_BOTTOM.getErrorCode());
        errorAttributes.put("message", error.getMessage());
        if (error instanceof CustomException) {
            errorAttributes.put("code", ((CustomException) error).getCode());
        }
        //todo 404的状态码需要修改下
        if (errorStatus == HttpStatus.NOT_FOUND) {
            errorAttributes.put("code", RouterErrorCode.ERROR_ROUTE_NOT_FOUND.getErrorCode());
        }

        return errorAttributes;
    }

    //从DefaultErrorWebExceptionHandler中复制过来的
    private HttpStatus determineHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getStatus();
        }

        return responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(HttpStatus.OK);
    }
}
