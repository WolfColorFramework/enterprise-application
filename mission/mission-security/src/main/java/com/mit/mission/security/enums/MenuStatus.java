package com.mit.mission.security.enums;

import lombok.Getter;

/**
 *  @autor: gaoy
 *  @date: 2021/6/17 9:50
 *  @description: 菜单状态
 */
@Getter
public enum MenuStatus {
    ENABLE(1, "启用"),
    DISABLE(2, "禁用");

    int code;
    String message;

    MenuStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
