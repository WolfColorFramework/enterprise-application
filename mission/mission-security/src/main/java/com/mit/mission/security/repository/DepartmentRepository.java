package com.mit.mission.security.repository;

import com.mit.mission.security.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String>, JpaSpecificationExecutor<Department> {

    List<Department> findByParentIdsLike(String parentIds);

    Department findOneByName(String name);

    @Query("select max(d.sort) from Department d where d.parentId = :parentId")
    Integer findMaxSort(@Param("parentId") String parentId);

    @Query("select d.parentIds from Department d where d.uuid = :uuid")
    String findParentIdsByUuid(@Param("uuid") String uuid);

}
