package com.mit.mission.security.dto;

import com.mit.mission.security.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 *  @autor: gaoy
 *  @date: 2021/4/22 17:24
 *  @description: 权限User
 */
@Data
public class SecurityUser implements UserDetails {

    // 当前登录用户
    private transient User currentUserInfo;

    // 用户的权限集合
    private Collection<? extends GrantedAuthority> authorities;

    // 用户按钮集合
    private List<String> btns;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public List<String> getBtns(){
        return btns;
    }

    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUserInfo.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
