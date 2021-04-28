package com.mit.mission.common.exception;

import lombok.Getter;

/**
 *  @autor: gaoy
 *  @date: 2021/4/19 9:55
 *  @description: 全局异常捕获
 */
@Getter
public class CustomException extends RuntimeException {

    private Integer code ;
    private String message;

    private CustomException(){}

    public CustomException(CustomExceptionType exceptionTypeEnum) {
        this.code = exceptionTypeEnum.getCode();
        this.message = exceptionTypeEnum.getDesc();
    }

    public CustomException(CustomExceptionType exceptionTypeEnum,
                           String message) {
        this.code = exceptionTypeEnum.getCode();
        this.message = message;
    }

}
