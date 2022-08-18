package com.cpp.supplychain.gateway.router.errorCode;


import com.cpp.supplychain.bss.commons.exception.CustomException;

/**
 * 路由错误码
 *
 * @author yuanzhi.wang
 */
public enum RouterErrorCode implements CustomException.ErrorCode {

    /*============兜底错误码============*/
    ERROR_BOTTOM("1000001", "网关服务错误"),
    /*============路由============*/

    ERROR_ROUTE_NOT_FOUND("1000002", "网关未匹配路由"),

    /*============校验============*/
    ERROR_TOKEN_EXP("1101000", "token已过期"),
    ERROR_TOKEN_ILLEGAL("1101001", "token非法"),
    ERROR_TOKEN_NOT_EXIST("1101002", "token不存在"),


    /*============限流、熔断============*/;


    private final String code;
    private final String message;

    RouterErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getErrorCode() {
        return this.code;
    }

    @Override
    public String getErrorMessage() {
        return this.message;
    }
}
