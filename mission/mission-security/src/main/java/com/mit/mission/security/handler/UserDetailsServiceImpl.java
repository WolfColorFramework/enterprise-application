package com.mit.mission.security.handler;

import com.mit.mission.security.domain.User;
import com.mit.mission.security.dto.SecurityUser;
import com.mit.mission.security.service.MenuService;
import com.mit.mission.security.service.RoleService;
import com.mit.mission.security.service.UserService;
import com.mit.mission.security.vo.MenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询数据
        User user = userService.findOneByAccount(username);
        //判断
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 深拷贝
        User curUser = new User();
        BeanUtils.copyProperties(user, curUser);
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(curUser);

        // 权限列表
        List<String> roleIds = Stream.of(curUser.getRoleIds().split(",")).collect(Collectors.toList());
        List<String> menus = menuService.getMenusByRoleIds(roleIds).stream().map(MenuVo::getUrl).collect(Collectors.toList());
        securityUser.setAuthorities(
                AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",",menus)));

        // btns
        List<String> btns = menuService.getBtnsByRoleIds(roleIds);
        securityUser.setBtns(btns);
        return securityUser;
    }
}
