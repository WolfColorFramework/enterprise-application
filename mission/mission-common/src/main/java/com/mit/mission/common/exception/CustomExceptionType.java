package com.mit.mission.common.exception;

import lombok.Getter;

/**
 * @autor: gaoy
 * @date: 2021/4/19 9:55
 * @description: 全局异常类型
 */
@Getter
public enum CustomExceptionType {

    USER_INPUT_ERROR(400, "输入异常"),
    SYSTEM_ERROR(500, "系统异常"),
    AUTHENTICATION_ERROR(600, "认证异常"),
    OTHER_ERROR(999, "未知异常");

    private Integer code;
    private String desc;

    CustomExceptionType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
