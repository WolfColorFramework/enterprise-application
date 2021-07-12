package com.mit.mission.security.repository;

import com.mit.mission.security.domain.Menu;
import com.mit.mission.security.vo.MenuVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {

    Menu findOneByCode(String code);

    @Query(value = "SELECT m1.uuid AS uuid, m1.icon AS icon, ( CASE WHEN (m2.uuid = '0' OR m2.uuid IS NULL) THEN '0' ELSE m2.uuid END ) AS parentId, m1.code AS code, m1.name AS name, m1.languageName AS languageName, m1.url AS url, m1.level AS level, m1.type AS type, m1.num AS num FROM sys_menu m1 LEFT JOIN sys_menu m2 ON m1.parentId = m2.uuid INNER JOIN ( SELECT uuid FROM sys_menu WHERE uuid IN ( SELECT r.menuId FROM sys_role_menu r WHERE r.roleId IN ( :roleIds ) ) ) m3 ON m1.uuid = m3.uuid WHERE m1.type = 0 AND m1.status = 1 ORDER BY level, num ASC", nativeQuery = true)
    List<MenuVo> getMenusByRoleIds(@Param("roleIds") List<String> roleIds);

    @Query(value = "SELECT m1.code AS code FROM sys_menu m1 LEFT JOIN sys_menu m2 ON m1.parentId = m2.uuid INNER JOIN ( SELECT uuid FROM sys_menu WHERE uuid IN ( SELECT r.menuId FROM sys_role_menu r WHERE r.roleId IN ( :roleIds ) ) ) m3 ON m1.uuid = m3.uuid WHERE m1.type = 1 AND m1.status = 1", nativeQuery = true)
    List<String> getBtnsByRoleIds(@Param("roleIds") List<String> roleIds);

    @Query("select max(m.num) from Menu m Where m.parentId = :parentId")
    Integer findMaxSort(@Param("parentId") String parentId);

    List<Menu> findAllByType(Integer type);

    List<Menu> findAllByStatus(Integer status);
}
