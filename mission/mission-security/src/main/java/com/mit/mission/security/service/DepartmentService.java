package com.mit.mission.security.service;

import java.util.List;

import com.mit.mission.security.domain.Department;
import com.mit.mission.security.dto.DepartmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
	<S extends Department> S add(S entity);

	Boolean update(Department department);

	Department findOneByUuid(String uuid);

	Department findOneByName(String name);

	List<DepartmentDto> tree() throws Exception;

	List<Department> findByUuids(String uuids);

	List<Department> findByParentIdsLike(String parentIds);

	void delete(String uuids);

	Page<Department> findAll(Pageable pageable);

	List<Department> findAll();

	Integer findMaxSort(String parentId);

	String findParentIdsByUuid(String uuid);
}
