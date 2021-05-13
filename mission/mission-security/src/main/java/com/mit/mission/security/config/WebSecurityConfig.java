package com.mit.mission.security.config;

import com.mit.mission.security.filter.TokenAuthFilter;
import com.mit.mission.security.filter.TokenLoginFilter;
import com.mit.mission.security.handler.UnAuthEntryPoint;
import com.mit.mission.security.properties.SecurityProperties;
import com.mit.mission.security.util.DefaultPasswordEncoder;
import com.mit.mission.security.handler.TokenLogoutHandler;
import com.mit.mission.security.util.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *  @autor: gaoy
 *  @date: 2021/4/25 11:03
 *  @description: Security配置类
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private TokenManager tokenManager;
    private DefaultPasswordEncoder defaultPasswordEncoder;
    private UserDetailsService userDetailsService;
    private SecurityProperties securityProperties;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService, DefaultPasswordEncoder defaultPasswordEncoder,
                                  TokenManager tokenManager, SecurityProperties securityProperties) {
        this.userDetailsService = userDetailsService;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.tokenManager = tokenManager;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 允许iframe嵌套，解决：frame because it set 'X-Frame-Options' to 'deny 问题
        http.headers().frameOptions().disable();

        // 开放所有前置请求
        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest)
                .permitAll();

        http.csrf().disable()   // 关闭跨站攻击保护，否则无法跨域访问
                .exceptionHandling()
                .authenticationEntryPoint(new UnAuthEntryPoint())   // 认证失败处理器
            .and().cors()   // 开放跨域访问
            .and().httpBasic()
            .and().authorizeRequests()
                .antMatchers(securityProperties.getFreeUrls()).permitAll() // 可直接访问的url
                .anyRequest().authenticated()
            .and().logout().logoutUrl(securityProperties.getLogoutUrl())
            .addLogoutHandler(new TokenLogoutHandler(tokenManager))
            .and().addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, securityProperties))
                    .addFilter(new TokenAuthFilter(authenticationManager(), tokenManager, userDetailsService));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    // 静态资源路径
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(securityProperties.getStaticUrls());
    }

    // 跨域配置
    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(CorsConfiguration.ALL);
        configuration.addAllowedHeader(CorsConfiguration.ALL);
        configuration.addAllowedMethod(CorsConfiguration.ALL);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
