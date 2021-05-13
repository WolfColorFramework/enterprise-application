package com.mit.mission.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import com.mit.mission.common.web.ResponseMessage;
import com.mit.mission.common.util.ResponseUtil;
import com.mit.mission.security.domain.User;
import com.mit.mission.security.dto.SecurityUser;
import com.mit.mission.security.properties.SecurityProperties;
import com.mit.mission.security.util.TokenManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @autor: gaoy
 * @date: 2021/4/22 16:03
 * @description: token登录过滤器
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private TokenManager tokenManager;
    private AuthenticationManager authenticationManager;
    private SecurityProperties securityProperties;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager,
                            SecurityProperties securityProperties) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.securityProperties = securityProperties;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(securityProperties.getLoginUrl(), "POST"));
    }

    //1 获取表单提交用户名和密码
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        //获取表单提交数据
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getAccount(), user.getPassword()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //2 认证成功调用的方法
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult) {
        //认证成功，得到认证成功之后用户信息
        SecurityUser user = (SecurityUser) authResult.getPrincipal();
        //根据用户名生成token
        String token = tokenManager.generateToken(user);
        //返回token
        ResponseUtil.out(response, ResponseMessage.OK("成功", token));
    }

    //3 认证失败调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        ResponseUtil.out(response, ResponseMessage.Error(new CustomException(CustomExceptionType.AUTHENTICATION_ERROR)));
    }
}
