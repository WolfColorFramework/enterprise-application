package com.mit.mission.security.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.alibaba.fastjson.JSONObject;
import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import com.mit.mission.common.util.AutoMapper;
import com.mit.mission.common.util.BeanKit;
import com.mit.mission.security.domain.Menu;
import com.mit.mission.security.dto.MenuDto;
import com.mit.mission.security.enums.MenuEnum;
import com.mit.mission.security.repository.MenuRepository;
import com.mit.mission.security.service.MenuService;

import com.mit.mission.security.util.SecurityKit;
import com.mit.mission.security.util.ToolUtils;
import com.mit.mission.security.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public <S extends Menu> S save(S entity) {
        // 判断是否存在该编号
        Menu dataMenu = this.findOneByCode(entity.getCode());
        if (dataMenu != null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "菜单编号已经存在");
        }

        // 设置父级菜单编号
        menuSetPcode(entity);

        // 设置信息
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateUserId(SecurityKit.getUser().getUuid());
        entity.setUpdateUserId(SecurityKit.getUser().getUuid());

        S menu = menuRepository.save(entity);
        if (menu.getUuid() != null) {
            List<Menu> subMenus = menuRepository.findAll((Specification<Menu>) (root, query, cb) -> {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(cb.like(root.get("parentIds").as(String.class), "%[" + entity.getUuid() + "]%"));
                Predicate[] pre = new Predicate[list.size()];
                return query.where(list.toArray(pre)).getRestriction();
            });
            if (!CollectionUtils.isEmpty(subMenus)) {
                for (Menu subMenu : subMenus) {
                    menuSetPcode(subMenu);
                    subMenu.setUpdateTime(new Date());
                    subMenu.setUpdateUserId(SecurityKit.getUser().getUuid());
                    menuRepository.save(subMenu);
                }
            }
        }
        return menu;
    }

    @Override
    public Menu update(Menu menu) {

        Menu dataMenu = this.findOneByUuid(menu.getUuid());
        // 菜单编号修改了
        if (!menu.getCode().equals(dataMenu.getCode())) {
            // 判断是否存在该编号
            Menu theMenu = this.findOneByCode(menu.getCode());
            if (theMenu != null) {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "菜单编号已经存在");
            }
        }

        if (menu.getUuid().equals(menu.getParentId())) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "不能选择自己作为上级菜单");
        }

        if (dataMenu.getParentId() != null) {
            Menu parentMenu = this.findOneByUuid(dataMenu.getParentId());
            if (parentMenu != null && parentMenu.getParentIds() != null && parentMenu.getParentIds().contains(menu.getUuid())) {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "不能选择自己的下级作为上级菜单");
            }
        }

        menuSetPcode(menu);
        menu.setUpdateTime(new Date());
        menu.setUpdateUserId(SecurityKit.getUser().getUuid());
        return menuRepository.save(menu);
    }

    @Override
    public Menu findOneByUuid(String uuid) {
        return menuRepository.findById(uuid).get();
    }

    @Override
    public Page<Menu> findAll(Pageable pageable) {
        return menuRepository.findAll(pageable);
    }

    @Override
    public void delete(String uuids) {

        if (StringUtils.isEmpty(uuids)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }

        String[] ids = uuids.split(",");
        List<String> mustMenuIds = MenuEnum.getMustMenuIds();
        for (String id : ids) {
            if (mustMenuIds.contains(id)) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "系统菜单不可删除");
            }
        }

        for (String uuid : ids) {
            menuRepository.deleteById(uuid);
        }
    }

    @Override
    public List<Menu> findByUuids(String uuids) {
        List<Menu> menus = new ArrayList<>();
        String[] ids = uuids.split(",");
        for (String uuid : ids) {
            menus.add(menuRepository.getOne(uuid));
        }
        return menus;
    }

    @Override
    public Menu findOneByCode(String code) {
        return menuRepository.findOneByCode(code);
    }

    @Override
    public Map<String, Object> findMenuToMapById(String uuid) {
        Menu menu = this.findOneByUuid(uuid);
        String parentIds = menu.getParentIds().replace("[", "").replace("]", "");
        menu.setParentIds(parentIds);
        Map<String, Object> menuMap = BeanKit.beanToMap(menu);
        menuMap.put("parent_ids", parentIds.split(","));
        return menuMap;
    }

    @Override
    public void dragSortMenus(String menuIds, String sorts) {

        if (StringUtils.isEmpty(menuIds)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }

        if (StringUtils.isEmpty(sorts)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }

        String[] ids = menuIds.split(",");
        String[] nums = sorts.split(",");

        for (int i = 0; i < ids.length; i++) {
            Menu menu = menuRepository.getOne(ids[i]);
            menu.setNum(Integer.valueOf(nums[i]));
            menu.setUpdateTime(new Date());
            menu.setUpdateUserId(SecurityKit.getUser().getUuid());
            menuRepository.save(menu);
        }
    }

    @Override
    public void updateStatus(String menuIds, Integer status) {

        if (StringUtils.isEmpty(menuIds)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }

        String[] ids = menuIds.split(",");
        if (status == 2) {
            List<String> mustMenuIds = MenuEnum.getMustMenuIds();
            for (String id : ids) {
                if (mustMenuIds.contains(id)) {
                    throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "系统菜单不可禁用");
                }
            }
        }

        for (String id : ids) {
            Menu menu = menuRepository.getOne(id);
            menu.setStatus(status);
            menu.setUpdateTime(new Date());
            menu.setUpdateUserId(SecurityKit.getUser().getUuid());
            menuRepository.save(menu);
        }
    }

    @Override
    public List<MenuVo> getMenusByRoleIds(List<String> roleIds) {
        if (roleIds.size() < 1) {
            return null;
        }
        return menuRepository.getMenusByRoleIds(roleIds);
    }

    @Override
    public List<String> getBtnsByRoleIds(List<String> roleIds) {
        if (roleIds.size() < 1) {
            return null;
        }
        return menuRepository.getBtnsByRoleIds(roleIds);
    }

    @Override
    public List<Menu> findAllMenus(Menu menu) {
        return menuRepository.findAll(new Specification<Menu>() {
            @Override
            public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (!StringUtils.isEmpty(menu.getName())) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + menu.getName().replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(menu.getCode())) {
                    list.add(cb.like(root.get("code").as(String.class), "%" + menu.getCode().replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(menu.getUrl())) {
                    list.add(cb.like(root.get("url").as(String.class), "%" + menu.getUrl().replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                Predicate[] pre = new Predicate[list.size()];
                return query.where(list.toArray(pre)).getRestriction();
            }
        });
    }

    @Override
    public List<Menu> findAllMenus() {
        // 查询菜单节点
        return menuRepository.findAllByType(0);
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public List<MenuDto> list(Menu menu) throws Exception {
        List<Menu> menus = this.findAllMenus(menu);

        List<MenuDto> menuDtos = AutoMapper.mapperList(menus, MenuDto.class, (c, v) -> {
            v.setLabel(c.getName());
            v.setValue(c.getUuid());
        });

        List list = ToolUtils.factorTree(menuDtos);
        return list;
    }

    @Override
    public List<MenuDto> menuTree() throws Exception {
        List<Menu> menus = this.findAllMenus();
        List<MenuDto> menuDtos = AutoMapper.mapperList(menus, MenuDto.class, (c, v) -> {
            v.setLabel(c.getName());
            v.setValue(c.getUuid());
        });
        List list = ToolUtils.factorTree(menuDtos);
        return list;
    }

    @Override
    public Integer findMaxSort(String parentId) {
        return menuRepository.findMaxSort(parentId);
    }

    @Override
    public List<Menu> findAllByStatus(Integer status) {
        return menuRepository.findAllByStatus(status);
    }

    /**
     * 根据请求的父级菜单编号设置pcode和层级
     */
    private void menuSetPcode(Menu menu) {
        if (StringUtils.isEmpty(menu.getParentId()) || "0".equals(menu.getParentId())) {
            menu.setParentId("0");
            menu.setParentIds("[0],");
            menu.setLevel(1);
        } else {
            Menu pMenu = this.findOneByUuid(menu.getParentId());
            Integer pLevels = pMenu.getLevel();
            menu.setParentId(pMenu.getUuid());
            // 如果编号和父编号一致会导致无限递归
            if (menu.getCode().equals(pMenu.getCode())) {
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "菜单编号和副编号不能一致");
            }
            menu.setLevel(pLevels + 1);
            menu.setParentIds(pMenu.getParentIds() + "[" + pMenu.getUuid() + "],");
        }
    }
}
