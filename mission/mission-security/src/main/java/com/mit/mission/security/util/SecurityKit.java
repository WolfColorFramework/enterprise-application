package com.mit.mission.security.util;

import com.mit.mission.security.domain.User;
import com.mit.mission.security.dto.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @autor: gaoy
 * @date: 2021/4/23 18:08
 * @description: security工具类
 */
public class SecurityKit {

    public static User getUser() {
        SecurityUser securityUser = getSecurityUser();
        if (securityUser != null) {
            return securityUser.getCurrentUserInfo();
        }
        return null;
    }

    public static SecurityUser getSecurityUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal != null) return (SecurityUser) principal;
        }
        return null;
    }
}
