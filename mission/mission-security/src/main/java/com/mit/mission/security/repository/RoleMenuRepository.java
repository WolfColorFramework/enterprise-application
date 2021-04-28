package com.mit.mission.security.repository;

import com.mit.mission.security.domain.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, String> {

    @Query(value = "select url from sys_role_menu rel inner join sys_menu m on rel.menuId = m.uuid where rel.roleId = :uuid", nativeQuery = true)
    List<String> getResUrlsByRoleId(@Param("uuid") String uuid);

    @Query("select r.menuId from RoleMenu r where r.roleId = :roleId")
    List<String> findMenusIdsByRoleId(@Param("roleId") String roleId);

    @Modifying
    @Query("delete from RoleMenu r where r.roleId = :roleId")
    void deleteAllByRoleId(@Param("roleId") String roleId);

}
