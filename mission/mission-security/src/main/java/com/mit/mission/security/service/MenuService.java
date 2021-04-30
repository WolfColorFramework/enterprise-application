package com.mit.mission.security.service;

import java.util.List;
import java.util.Map;

import com.mit.mission.security.domain.Menu;
import com.mit.mission.security.dto.MenuDto;
import com.mit.mission.security.vo.MenuVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuService {
	<S extends Menu> S save(S entity);

	Menu update(Menu menu);

	Menu findOneByUuid(String uuid);

	Menu findOneByCode(String code);

	Map<String, Object> findMenuToMapById(String uuid);

	List<Menu> findByUuids(String uuids);

	void delete(String uuids);

	Page<Menu> findAll(Pageable pageable);

	void dragSortMenus(String menuIds, String sorts);

	void updateStatus(String menuIds, Integer status);

	List<MenuVo> getMenusByRoleIds(List<String> roleIds);

	List<String> getBtnsByRoleIds(List<String> roleList);

	List<Menu> findAllMenus(Menu menu);
	
	List<Menu> findAllMenus();
	
	List<Menu> findAll();

	List<MenuDto> list(Menu menu) throws Exception;

	List<MenuDto> menuTree() throws Exception;
	
	Integer findMaxSort(String parentId);

	List<Menu> findAllByStatus(Integer status);
}
