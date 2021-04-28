package com.mit.mission.security.util;

import com.mit.mission.common.util.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *  @autor: gaoy
 *  @date: 2021/4/22 14:07
 *  @description: 密码加密器
 */
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

    public DefaultPasswordEncoder() {
        this(-1);
    }

    public DefaultPasswordEncoder(int strength) {
    }
    //进行MD5加密
    @Override
    public String encode(CharSequence charSequence) {
        return MD5.encrypt(charSequence.toString());
    }
    //进行密码比对
    @Override
    public boolean matches(CharSequence charSequence, String encodedPassword) {
        return encodedPassword.equals(MD5.encrypt(charSequence.toString()));
    }
}
