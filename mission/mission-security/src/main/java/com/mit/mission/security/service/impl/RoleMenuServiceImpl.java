package com.mit.mission.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mit.mission.security.domain.RoleMenu;
import com.mit.mission.security.repository.RoleMenuRepository;
import com.mit.mission.security.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {
	@Autowired
	private RoleMenuRepository roleMenuRepository;

	@Override
	public <S extends RoleMenu> S save(S entity) {
		return roleMenuRepository.save(entity);
	}

	@Override
	public RoleMenu findOneByUuid(String uuid) {
		return roleMenuRepository.getOne(uuid);
	}

	@Override
	public Page<RoleMenu> findAll(Pageable pageable) {
		return roleMenuRepository.findAll(pageable);
	}

	@Override
	public void delete(String uuids) {
		String[] ids = uuids.split(",");
		for (String uuid : ids) {
			roleMenuRepository.deleteById(uuid);
		}
	}

	@Override
	public List<RoleMenu> findByUuids(String uuids) {
		List<RoleMenu> relations = new ArrayList<>();
		String[] ids = uuids.split(",");
		for (String uuid : ids) {
			relations.add(roleMenuRepository.getOne(uuid));
		}
		return relations;
	}

	@Override
	public List<String> getResUrlsByRoleId(String uuid) {
		return roleMenuRepository.getResUrlsByRoleId(uuid);
	}

	@Override
	public List<String> findMenusIdsByRoleId(String roleId) {
		return roleMenuRepository.findMenusIdsByRoleId(roleId);
	}

	@Override
	public void deleteAllByRoleId(String roleId) {
		roleMenuRepository.deleteAllByRoleId(roleId);
	}
}
