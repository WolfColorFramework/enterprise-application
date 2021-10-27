package com.mit.mission.security.handler;

import com.mit.mission.common.web.ResponseMessage;
import com.mit.mission.common.util.ResponseUtil;
import com.mit.mission.security.util.TokenManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  @autor: gaoy
 *  @date: 2021/4/22 15:18 
 *  @description: 退出登录处理器
 */
public class TokenLogoutHandler implements LogoutHandler {
    private TokenManager tokenManager;

    public TokenLogoutHandler(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //1 从header里面获取token
        //2 token不为空，移除token
        String token = request.getHeader(tokenManager.getHeader());
        if(token != null) {
            //移除
            tokenManager.removeToken(token);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        ResponseUtil.out(response, ResponseMessage.OK("成功"));
    }
}
