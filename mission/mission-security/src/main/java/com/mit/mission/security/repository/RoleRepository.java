package com.mit.mission.security.repository;

import com.mit.mission.security.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    Role findOneByName(String name);

    @Query("select max(r.sort) from Role r")
    Integer findMaxSort();

    @Query("from Role r order by r.updateTime desc")
    List<Role> findAllOrderByUpdateTime();

    List<Role> findAllByDepartmentId(String departmentId);

}
