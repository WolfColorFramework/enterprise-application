package com.mit.mission.security.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import com.mit.mission.common.util.BeanKit;
import com.mit.mission.common.util.StrKit;
import com.mit.mission.core.base.Const;
import com.mit.mission.core.base.domain.Argument;
import com.mit.mission.core.base.repository.ArgumentRepository;
import com.mit.mission.core.traffic.domain.Configuration;
import com.mit.mission.core.traffic.domain.Line;
import com.mit.mission.core.traffic.domain.Station;
import com.mit.mission.core.traffic.repository.ConfigurationRepository;
import com.mit.mission.core.traffic.repository.LineRepository;
import com.mit.mission.core.traffic.dto.TrafficTree;
import com.mit.mission.core.traffic.repository.StationRepository;
import com.mit.mission.security.domain.Role;
import com.mit.mission.security.domain.RoleMenu;
import com.mit.mission.security.domain.RoleStation;
import com.mit.mission.security.repository.RoleMenuRepository;
import com.mit.mission.security.repository.RoleRepository;
import com.mit.mission.security.repository.RoleStationRepository;
import com.mit.mission.security.service.DepartmentService;
import com.mit.mission.security.service.RoleService;

import com.mit.mission.security.util.SecurityKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private RoleStationRepository roleStationRepository;
    @Autowired
    private ArgumentRepository argumentRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;
    @Autowired
    private DepartmentService departmentService;

    @Override
    public <S extends Role> S save(S entity) {

        // 判断角色名称是否重复
        Role theRole = this.findOneByName(entity.getName());
        if (theRole != null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该角色已经存在");
        }

        // 获取同级角色的最大排序号
        Integer maxSort = this.findMaxSort();
        if (maxSort == null) {
            maxSort = 1;
        } else {
            maxSort++;
        }

        entity.setSort(maxSort);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateUserId(SecurityKit.getUser().getUuid());
        entity.setUpdateUserId(SecurityKit.getUser().getUuid());

        return roleRepository.save(entity);
    }

    @Override
    public Role findOneByUuid(String uuid) {
        return roleRepository.getOne(uuid);
    }

    @Override
    public Map<String, Object> findOneToMap(String uuid) {
        Role role = this.findOneByUuid(uuid);
        String parentIds = departmentService.findParentIdsByUuid(role.getDepartmentId());
        Map<String, Object> map = BeanKit.beanToMap(role);
        map.put("parent_ids", (parentIds != null ? parentIds.replace("[", "").replace("]", "").split(",") : new ArrayList<String>()));
        return map;
    }

    @Override
    public Page<Role> findAll(Pageable pageable, Map<String, Object> condition) {
        return roleRepository.findAll((root, query, cb) -> {
            if (null != condition) {
                List<Predicate> list = new ArrayList<>();

                if (!StringUtils.isEmpty(condition.get("name"))) {
                    list.add(cb.like(root.get("name").as(String.class),
                            "%" + ((String) condition.get("name")).replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("departId"))) {
                    list.add(cb.equal(root.get("departId").as(String.class), condition.get("departId")));
                }
                Predicate[] pre = new Predicate[list.size()];
                return query.where(list.toArray(pre)).getRestriction();
            }
            return null;
        }, pageable);
    }

    @Override
    public void delete(String uuids) {

        if (StringUtils.isEmpty(uuids)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }

        // 不能删除超级管理员角色
        String[] ids = uuids.split(",");
        for (String id : ids) {
            if (id.equals(Const.ADMIN_ROLE_ID)) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "不能删除超级管理员");
            }
        }

        for (String uuid : ids) {
            roleRepository.deleteById(uuid);
        }
    }

    @Override
    public List<Role> findByUuids(String uuids) {
        List<Role> roles = new ArrayList<>();
        String[] ids = uuids.split(",");
        for (String uuid : ids) {
            roles.add(roleRepository.getOne(uuid));
        }
        return roles;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAllOrderByUpdateTime();
    }

    @Override
    public Role findOneByName(String name) {
        return roleRepository.findOneByName(name);
    }

    @Override
    public Integer findMaxSort() {
        return roleRepository.findMaxSort();
    }

    @Override
    public void updateAuthority(String roleId, String ids) {
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(ids)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }

        // 删除该角色所有的权限
        roleMenuRepository.deleteAllByRoleId(roleId);
        String[] menuIds = ids.split(",");
        // 添加新的权限
        for (String id : menuIds) {
            RoleMenu relation = new RoleMenu();
            relation.setRoleId(roleId);
            relation.setMenuId(id);
            roleMenuRepository.save(relation);
        }
    }

    @Override
    public void updateStation(String roleId, List<Map<String, String>> param) {
        if (StringUtils.isEmpty(roleId) || param == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }

        roleStationRepository.deleteAllByRoleId(roleId);
        if (!CollectionUtils.isEmpty(param)) {
            for (Map<String, String> map : param) {
                RoleStation roleStation = new RoleStation();
                roleStation.setRoleId(roleId);
                roleStation.setLineId(StrKit.getValue(map.get("lineId")));
                roleStation.setStationId(StrKit.getValue(map.get("stationId")));
                roleStationRepository.save(roleStation);
            }
        }
    }

    @Override
    public List<String> findStationsByRoleId(String roleId) {

        if (StringUtils.isEmpty(roleId)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }

        List<String> keys = new ArrayList<String>();
        List<String> lineIds = new ArrayList<String>();
        List<RoleStation> roleStations = roleStationRepository.findAllByRoleId(roleId);
        if (!CollectionUtils.isEmpty(roleStations)) {
            for (RoleStation roleStation : roleStations) {
                keys.add(roleStation.getLineId() + "_" + roleStation.getStationId());
                if (!lineIds.contains(roleStation.getLineId())) {
                    lineIds.add(roleStation.getLineId());
                }
            }
            Argument cityConfig = argumentRepository.findOneByName(Const.PCC_CITY);
            if (cityConfig != null) {
                keys.add(cityConfig.getValue() + "_" + cityConfig.getUuid());
                for (String lineId : lineIds) {
                    Line line = lineRepository.getOne(lineId);
                    if (line != null) {
                        keys.add(line.getCode() + "_" + line.getUuid());
                    }
                }
            }

        }
        return keys;
    }

    @Override
    public TrafficTree findAllStationTree() {


        // 获取城市
        Argument cityConfig = argumentRepository.findOneByName(Const.PCC_CITY);
        if (cityConfig == null) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR);
        }
        TrafficTree metroTree = new TrafficTree();
        metroTree.setId(cityConfig.getUuid());
        metroTree.setLabel(cityConfig.getValue());
        metroTree.setType("CITY");
        metroTree.setLineId("0");
        metroTree.setStatId("0");
        metroTree.setKey(cityConfig.getValue() + "_" + cityConfig.getUuid());
        List<TrafficTree> lineTreeList = new ArrayList<TrafficTree>();
        // 获取线路
        List<Line> lineList = lineRepository.findAllOrderByNameAsc();
        if (null != lineList && lineList.size() > 0) {
            for (Line line : lineList) {
                TrafficTree lineTree = new TrafficTree();
                lineTree.setId(line.getUuid());
                lineTree.setLabel(line.getName());
                lineTree.setType("LINE");
                lineTree.setLineId(line.getUuid());
                lineTree.setStatId("0");
                lineTree.setKey(line.getCode() + "_" + line.getUuid());
                lineTreeList.add(lineTree);
            }
        }
        metroTree.setChildren(lineTreeList);
        // 获取车站
        for (TrafficTree metroTreeL : lineTreeList) {
            List<TrafficTree> stationTreeList = new ArrayList<TrafficTree>();
            List<Configuration> stationList = configurationRepository.findByLineIdAndDirection(metroTreeL.getId(), Const.DIRECTION_UP);
            if (null != stationList && stationList.size() > 0) {
                for (Configuration item : stationList) {
                    Station station = stationRepository.getOne(item.getStationId());
                    TrafficTree stationTree = new TrafficTree();
                    stationTree.setId(station.getUuid());
                    stationTree.setLabel(station.getName());
                    stationTree.setType("STATION");
                    stationTree.setLineId(metroTreeL.getId());
                    stationTree.setStatId(station.getUuid());
                    stationTree.setKey(metroTreeL.getId() + "_" + station.getUuid());
                    stationTreeList.add(stationTree);
                }
                metroTreeL.setChildren(stationTreeList);
            }
        }
        return metroTree;
    }
}
