package com.mit.mission.security.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import com.mit.mission.common.util.AES;
import com.mit.mission.common.util.DateUtil;
import com.mit.mission.common.properties.ConstProperties;

import com.mit.mission.security.domain.Role;
import com.mit.mission.security.properties.SecurityProperties;
import com.mit.mission.security.domain.User;
import com.mit.mission.security.enums.AccountStatus;
import com.mit.mission.security.repository.UserRepository;
import com.mit.mission.security.service.RoleService;
import com.mit.mission.security.service.UserService;
import com.mit.mission.security.util.SecurityKit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private ConstProperties constProperties;

    @Override
    public <S extends User> S save(S entity) {
        // 判断账号是否重复
        User theUser = this.findOneByAccount(entity.getAccount());
        if (theUser != null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户已存在");
        }

        entity.setPasswordStatus(0);
        entity.setExpiredTime(DateUtil.getAfterDayDates(securityProperties.getPasswordDay().toString()));
        entity.setPassword(AES.encrypt(securityProperties.getDefaultPWD()));
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateUserId(SecurityKit.getUser().getUuid());
        entity.setCreateUserName(SecurityKit.getUser().getAccount());
        entity.setUpdateUserId(SecurityKit.getUser().getUuid());
        entity.setCreateUserName(SecurityKit.getUser().getAccount());
        return userRepository.save(entity);
    }

    @Override
    public Boolean update(User user) {
        // 账号被修改，且被修改的账号已存在
        User dataUser = this.findOneByUuid(user.getUuid());
        if (!user.getAccount().equals(dataUser.getAccount())) {
            User theUser = this.findOneByAccount(user.getAccount());
            // 判断账号是否重复
            if (theUser != null) {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户已经存在");
            }
        }

        // 邮箱被修改了，且被修改的邮箱已存在
        if (!user.getEmail().equals(dataUser.getEmail())) {
            // 判断邮箱是否已经注册
            User theUser2 = this.findOneByEmail(user.getEmail());
            if (theUser2 != null) {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "邮箱已经存在");
            }
        }

        dataUser.setAccount(user.getAccount());
        dataUser.setName(user.getName());
        dataUser.setSex(user.getSex());
        dataUser.setEmail(user.getEmail());
        dataUser.setPhone(user.getPhone());
        dataUser.setStatus(user.getStatus());
        dataUser.setDepartId(user.getDepartId());
        dataUser.setDepartIdPath(user.getDepartIdPath());
        dataUser.setPasswordStatus(user.getPasswordStatus());
        dataUser.setExpiredTime(user.getExpiredTime());
        dataUser.setUpdateTime(new Date());
        dataUser.setUpdateUserId(SecurityKit.getUser().getUuid());
        userRepository.save(dataUser);
        return true;
    }

    @Override
    public User findOneByUuid(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }
        return userRepository.getOne(uuid);
    }

    @Override
    public Page<User> findAll(Pageable pageable, Map<String, Object> condition) {
        return userRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (!securityProperties.getAdminOpen()) {
                list.add(cb.notEqual(root.get("uuid").as(String.class), constProperties.getAdminId()));
            }
            if (null != condition) {
                if (!StringUtils.isEmpty(condition.get("account"))) {
                    list.add(cb.like(root.get("account").as(String.class), "%" + ((String) condition.get("account")).replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("name"))) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + ((String) condition.get("name")).replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("phone"))) {
                    list.add(cb.like(root.get("phone").as(String.class), "%" + ((String) condition.get("phone")).replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("departId"))) {
                    list.add(cb.like(root.get("departIdPath").as(String.class), "%" + condition.get("departId") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("beginTime"))) {
                    list.add(cb.greaterThanOrEqualTo(root.get("updateTime").as(String.class), condition.get("beginTime") + " 00:00:00"));
                }
                if (!StringUtils.isEmpty(condition.get("endTime"))) {
                    list.add(cb.lessThanOrEqualTo(root.get("updateTime").as(String.class), condition.get("endTime") + " 23:59:59"));
                }
            }
            Predicate[] pre = new Predicate[list.size()];
            return query.where(list.toArray(pre)).getRestriction();
        }, pageable);
    }

    @Override
    public User findOneByAccount(String username) {
        return userRepository.findOneByAccount(username);
    }

    @Override
    public User findOneByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    public void delete(String uuids) {
        // 不能删除超级管理员
        String[] ids = uuids.split(",");
        for (String id : ids) {
            if (id.equals(constProperties.getAdminId())) {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "请求参数错误");
            }
        }

        for (String uuid : ids) {
            userRepository.deleteById(uuid);
        }
    }

    @Override
    public void resetPassword(String uuids) {
        String[] ids = uuids.split(",");
        for (String uuid : ids) {
            User user = userRepository.getOne(uuid);
            user.setPasswordStatus(0);
            user.setExpiredTime(DateUtil.getAfterDayDates(securityProperties.getPasswordDay().toString()));
            user.setPassword(AES.encrypt(securityProperties.getDefaultPWD()));
            user.setUpdateTime(new Date());
            user.setUpdateUserId(SecurityKit.getUser().getUuid());
            user.setUpdateUserName(SecurityKit.getUser().getAccount());
            userRepository.save(user);
        }
    }

    @Override
    public void freeze(String uuids) {
        // 不能冻结超级管理员
        String[] ids = uuids.split(",");
        for (String id : ids) {
            if (id.equals(constProperties.getAdminId())) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "不能冻结超级管理员");
            }
        }

        for (String uuid : ids) {
            User user = userRepository.getOne(uuid);
            user.setStatus(AccountStatus.FREEZE.getCode());
            userRepository.save(user);
        }
    }

    @Override
    public void unfreeze(String uuids) {
        String[] ids = uuids.split(",");
        for (String uuid : ids) {
            User user = userRepository.getOne(uuid);
            if (user != null) {
                user.setStatus(AccountStatus.OK.getCode());
                userRepository.save(user);
            }
        }
    }

    @Override
    public void updateRoles(String uuids, String roleIds) {
        // 不能修改超级管理员
        String[] ids = uuids.split(",");
        for (String id : ids) {
            if (id.equals(constProperties.getAdminId())) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "不能修改超级管理员权限");
            }
        }

        for (String uuid : ids) {
            User user = userRepository.getOne(uuid);
            if (user != null) {
                user.setRoleIds(roleIds);
                userRepository.save(user);
            }
        }
    }

    @Override
    public List<Role> getRolesByUserId(String uuid) {
        User user = this.findOneByUuid(uuid);
        String roleIds = user.getRoleIds();
        List<Role> roles = null;
        if (!StringUtils.isEmpty(roleIds)) {
            roles = roleService.findByUuids(roleIds);
        }
        return roles;
    }

    @Override
    public Boolean setUserDepartment(String userId, String departmentId, String departmentIdPath) {

        if (StringUtils.isEmpty(userId)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户Id不可为空");
        }

        if (StringUtils.isEmpty(departmentId)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "部门Id不可为空");
        }

        if (StringUtils.isEmpty(departmentIdPath)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "部门路径不可为空");
        }

        User user = this.findOneByUuid(userId);
        if(user == null){
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "用户不存在");
        }

        user.setDepartId(departmentId);
        user.setDepartIdPath(departmentIdPath);
        this.save(user);

        return true;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll((Specification<User>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate[] pre = new Predicate[list.size()];
            return query.where(list.toArray(pre)).getRestriction();
        });
    }

    @Override
    public List<User> findByUuidIn(List<String> ids) {
        return userRepository.findByUuidIn(ids);
    }

    @Override
    public List<User> findByUuidInAndNameLike(List<String> ids, String name) {
        return userRepository.findByUuidInAndNameLike(ids, name);
    }

    @Override
    public void lock(String userIds) {
        // 不能锁定超级管理员
        String[] ids = userIds.split(",");
        for (String id : ids) {
            if (id.equals(String.valueOf(constProperties.getAdminId()))) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "不能锁住超级管理员");
            }
        }

        for (String uuid : ids) {
            User user = userRepository.getOne(uuid);
            if (user != null) {
                user.setStatus(AccountStatus.LOCK.getCode());
                userRepository.save(user);
            }
        }
    }

    @Override
    public void unlock(String userIds) {
        String[] ids = userIds.split(",");
        for (String uuid : ids) {
            User user = userRepository.getOne(uuid);
            if (user != null) {
                user.setStatus(AccountStatus.UNLOCK.getCode());
                userRepository.save(user);
            }
        }
    }

    @Override
    public List<User> getUsersByDepart(String departId) {
        return userRepository.findAllByDepartId(departId);
    }


}
