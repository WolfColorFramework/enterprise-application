package com.mit.mission.security.service;

import java.util.List;
import java.util.Map;

import com.mit.mission.core.traffic.dto.TrafficTree;
import com.mit.mission.security.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
	<S extends Role> S save(S entity);

	Role findOneByUuid(String uuid);

	Map<String, Object> findOneToMap(String uuid);

	Role findOneByName(String name);

	List<Role> findByUuids(String uuids);

	void delete(String uuids);

	Page<Role> findAll(Pageable pageable, Map<String, Object> condition);

	List<Role> findAll();

	Integer findMaxSort();

	void updateAuthority(String roleId, String ids);

	void updateStation(String roleId, List<Map<String, String>> param);

	List<String> findStationsByRoleId(String roleId);

	TrafficTree findAllStationTree();
}
