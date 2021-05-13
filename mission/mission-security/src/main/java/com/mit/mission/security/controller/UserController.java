package com.mit.mission.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.mit.mission.common.web.PageParam;
import com.mit.mission.common.web.PageResult;
import com.mit.mission.common.web.ResponseMessage;
import com.mit.mission.security.domain.Role;
import com.mit.mission.security.domain.User;
import com.mit.mission.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  @autor: gaoy
 *  @date: 2021/4/27 13:59
 *  @description: 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseMessage getUserById(@PathVariable("userId") String userId) {
        User user = userService.findOneByUuid(userId);
        return ResponseMessage.OK("查询成功", user);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseMessage list(@RequestBody PageParam pageParam) {
        Page<User> userList = userService.findAll(pageParam.formatPageable(), pageParam.getCondition());
        PageResult pageResult = new PageResult(userList);
        pageResult.setRows(pageResult.getRows());
        return ResponseMessage.OK("查询成功", pageResult);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMessage add(@RequestBody User user) {
        userService.save(user);
        return ResponseMessage.OK("保存成功");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseMessage update(@RequestBody User user) {
        userService.update(user);
        return ResponseMessage.OK("修改成功");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseMessage delete(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String userIds = requestJson.getString("userIds");
        userService.delete(userIds);
        return ResponseMessage.OK("删除成功");
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseMessage resetPassword(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String userIds = requestJson.getString("userIds");
        userService.resetPassword(userIds);
        return ResponseMessage.OK("密码重置成功");
    }

    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    public ResponseMessage freeze(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String userIds = requestJson.getString("userIds");
        userService.freeze(userIds);
        return ResponseMessage.OK("冻结账号成功");
    }

    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    public ResponseMessage unfreeze(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String userIds = requestJson.getString("userIds");
        userService.unfreeze(userIds);
        return ResponseMessage.OK("账号解冻成功");
    }

    @RequestMapping(value = "/lock", method = RequestMethod.POST)
    public ResponseMessage lock(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String userIds = requestJson.getString("userIds");
        userService.lock(userIds);
        return ResponseMessage.OK("账号锁定成功");
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    public ResponseMessage unlock(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String userIds = requestJson.getString("userIds");
        userService.unlock(userIds);
        return ResponseMessage.OK("账号解锁成功");
    }

    @RequestMapping(value = "/set-role", method = RequestMethod.POST)
    public ResponseMessage setRole(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String userIds = requestJson.getString("userIds");
        String roleIds = requestJson.getString("roleIds");
        userService.updateRoles(userIds, roleIds);
        return ResponseMessage.OK("角色设置成功");
    }

    @RequestMapping(value = "/roles/{userId}", method = RequestMethod.GET)
    public ResponseMessage getUserRoles(@PathVariable("userId") String userId) {
        List<Role> roles = userService.getRolesByUserId(userId);
        return ResponseMessage.OK("查询成功", roles);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseMessage list() {
        List<User> userList = userService.findAll();
        return ResponseMessage.OK("查询成功", userList);
    }

    @RequestMapping(value = "/users-department/{departmentId}", method = RequestMethod.GET)
    public ResponseMessage getUsersByDepartment(@PathVariable("departmentId") String departmentId) {
        List<User> userList = userService.getUsersByDepart(departmentId);
        return ResponseMessage.OK("查询成功", userList);
    }

    @RequestMapping(value = "/set-department", method = RequestMethod.POST)
    public Object setDepartment(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String userId = requestJson.getString("userId");
        String departmentId = requestJson.getString("departmentId");
        String departmentIdPath = requestJson.getString("departmentIdPath");
        userService.setUserDepartment(userId, departmentId, departmentIdPath);
        return ResponseMessage.OK("修改成功");
    }
}
