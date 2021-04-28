package com.mit.mission.security.handler;

import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import com.mit.mission.common.web.ResponseMessage;
import com.mit.mission.common.util.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  @autor: gaoy
 *  @date: 2021/4/25 11:20
 *  @description: 认证失败处理器
 */
public class UnAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtil.out(response, ResponseMessage.Error(new CustomException(CustomExceptionType.AUTHENTICATION_ERROR)));
    }
}
