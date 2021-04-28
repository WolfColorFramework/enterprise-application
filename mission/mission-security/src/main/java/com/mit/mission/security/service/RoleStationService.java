package com.mit.mission.security.service;

import java.util.List;

public interface RoleStationService {

	List<String> getLineIdsByRoleIds(List<String> roleIds);
	
	List<String> getStationIdsByRoleIds(List<String> roleIds);

}
