package com.mit.mission.security.service;

import java.util.List;
import java.util.Map;

import com.mit.mission.security.domain.Role;
import com.mit.mission.security.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    <S extends User> S save(S entity);

    Boolean update(User user);

    User findOneByUuid(String uuid);

    User findOneByAccount(String username);

    User findOneByEmail(String email);

    Page<User> findAll(Pageable pageable, Map<String, Object> condition);

    void delete(String uuids);

    void resetPassword(String uuids);

    void freeze(String uuids);

    void unfreeze(String uuids);

    void updateRoles(String uuids, String roleIds);

    List<Role> getRolesByUserId(String uuid);

    Boolean setUserDepartment(String userId, String departmentId, String departmentIdPath);

    List<User> findAll();

    List<User> findByUuidIn(List<String> ids);

    List<User> findByUuidInAndNameLike(List<String> ids, String name);

    void lock(String userIds);

    void unlock(String userIds);

    List<User> getUsersByDepart(String departId);

}
