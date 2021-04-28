package com.mit.mission.security.filter;

import com.mit.mission.security.util.TokenManager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @autor: gaoy
 * @date: 2021/4/22 15:36
 * @description: token认证过滤器
 */
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private UserDetailsService userDetailsService;

    public TokenAuthFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取当前认证成功用户权限信息
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);
        //判断如果有权限信息，放到权限上下文中
        if (authRequest != null) {
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader(tokenManager.getHeader());
        if (token != null) {
            // 用户名
            String username = tokenManager.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (tokenManager.validateToken(token, userDetails)) {
                    //给使用该JWT令牌的用户进行授权
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                    return authenticationToken;
                }
            }
        }
        return null;
    }
}
