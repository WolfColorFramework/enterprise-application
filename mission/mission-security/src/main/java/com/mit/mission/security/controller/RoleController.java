package com.mit.mission.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.mit.mission.common.web.PageParam;
import com.mit.mission.common.web.PageResult;
import com.mit.mission.common.web.ResponseMessage;
import com.mit.mission.core.traffic.dto.TrafficTree;
import com.mit.mission.security.domain.Role;
import com.mit.mission.security.service.DepartmentService;
import com.mit.mission.security.service.MenuService;
import com.mit.mission.security.service.RoleMenuService;
import com.mit.mission.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @autor: gaoy
 * @date: 2021/4/28 9:41
 * @description: 角色控制器
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @Autowired
    DepartmentService departService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleMenuService roleMenuService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMessage add(@RequestBody Role role) {
        roleService.save(role);
        return ResponseMessage.OK("保存成功");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseMessage delete(@RequestBody String roleIds) {
        roleService.delete(roleIds);
        return ResponseMessage.OK("删除成功");
    }

    @RequestMapping(value = "/query/{roleId}", method = RequestMethod.GET)
    public ResponseMessage getRoleById(@PathVariable("roleId") String roleId) {
        Map<String, Object> roleMap = roleService.findOneToMap(roleId);
        return ResponseMessage.OK("查询成功", roleMap);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageResult list(@RequestBody PageParam pageParam) {
        Page<Role> roleList = roleService.findAll(pageParam.formatPageable(), pageParam.getCondition());
        PageResult pageResult = new PageResult(roleList);
        pageResult.setRows(pageResult.getRows());
        return pageResult;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseMessage list() {
        List<Role> roles = roleService.findAll();
        return ResponseMessage.OK("查询成功", roles);
    }

    @RequestMapping(value = "/set-authority", method = RequestMethod.POST)
    public ResponseMessage setAuthority(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String roleId = requestJson.getString("roleId");
        String ids = requestJson.getString("ids");

        roleService.updateAuthority(roleId, ids);
        return ResponseMessage.OK("修改成功");
    }

    @RequestMapping(value = "/set-station/{roleId}", method = RequestMethod.POST)
    public ResponseMessage setRoleStation(@PathVariable("roleId") String roleId, @RequestBody List<Map<String, String>> param) {
        roleService.updateStation(roleId, param);
        return ResponseMessage.OK("修改成功");
    }

    @RequestMapping(value = "/query-role-station/{roleId}", method = RequestMethod.GET)
    public ResponseMessage getRoleStations(@PathVariable("roleId") String roleId) {
        TrafficTree trafficTree = roleService.findAllStationTree();
        List<String> keys = roleService.findStationsByRoleId(roleId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("TrafficTree", trafficTree);
        map.put("keys", keys);
        return ResponseMessage.OK("查询成功", map);
    }
}
