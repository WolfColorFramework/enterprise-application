package com.mit.mission.security.enums;

import lombok.Getter;

/**
 *  @autor: gaoy
 *  @date: 2021/4/21 14:18
 *  @description: 账号状态
 */
@Getter
public enum AccountStatus {

    OK(1, "启用"),
    FREEZE(2, "冻结"),
    DELETED(3, "被删除"),
    LOCK(4, "锁定"),
    UNLOCK(5, "解锁");

    private Integer code;
    private String message;

    AccountStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String valueOf(Integer value) {
        if (value == null) {
            return "";
        } else {
            for (AccountStatus ms : AccountStatus.values()) {
                if (ms.getCode().equals(value)) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
