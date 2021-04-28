package com.mit.mission.security.service;

import java.util.List;

import com.mit.mission.security.domain.RoleMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleMenuService {
	<S extends RoleMenu> S save(S entity);

	RoleMenu findOneByUuid(String uuid);

	List<RoleMenu> findByUuids(String uuids);

	void delete(String uuids);

	Page<RoleMenu> findAll(Pageable pageable);

	List<String> getResUrlsByRoleId(String uuid);

	List<String> findMenusIdsByRoleId(String roleId);

	void deleteAllByRoleId(String roleId);
}
