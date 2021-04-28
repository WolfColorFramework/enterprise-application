package com.mit.mission.security.repository;

import com.mit.mission.security.domain.RoleStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleStationRepository extends JpaRepository<RoleStation, String>, JpaSpecificationExecutor<RoleStation> {

    List<RoleStation> findAllByRoleId(String roleId);

    @Modifying
    @Query("delete from RoleStation r where r.roleId = :roleId")
    void deleteAllByRoleId(@Param("roleId") String roleId);

}
