package com.mit.mission.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.mit.mission.common.web.ResponseMessage;
import com.mit.mission.security.domain.Menu;
import com.mit.mission.security.dto.MenuDto;
import com.mit.mission.security.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @autor: gaoy
 * @date: 2021/4/27 13:58
 * @description: 菜单控制器
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMessage add(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseMessage.OK("保存成功");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseMessage update(@RequestBody Menu menu) {
        menuService.update(menu);
        return ResponseMessage.OK("修改成功");
    }

    @RequestMapping(value = "/dragSort", method = RequestMethod.POST)
    public ResponseMessage dragSort(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String menuIds = requestJson.getString("menuIds");
        String sorts = requestJson.getString("sorts");
        menuService.dragSortMenus(menuIds, sorts);
        return ResponseMessage.OK("修改成功");
    }

    @RequestMapping(value = "/menu-to-map/{menuId}", method = RequestMethod.GET)
    public ResponseMessage getMenuById(@PathVariable("menuId") String menuId) {
        Map<String, Object> menuToMap = menuService.findMenuToMapById(menuId);
        return ResponseMessage.OK("查询成功", menuToMap);
    }

    @RequestMapping(value = "/set-status", method = RequestMethod.POST)
    public ResponseMessage setStatus(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String menuIds = requestJson.getString("menuIds");
        Integer status = requestJson.getInteger("status");
        menuService.updateStatus(menuIds, status);
        return ResponseMessage.OK("修改成功");
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseMessage list(@RequestBody Menu menu) throws Exception {
        List<MenuDto> list = menuService.list(menu);
        return ResponseMessage.OK("查询成功", list);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public ResponseMessage menuTree() throws Exception {
        List<MenuDto> list = menuService.menuTree();
        return ResponseMessage.OK("查询成功", list);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseMessage delete(@RequestBody String requestBody) {
        JSONObject requestJson = JSONObject.parseObject(requestBody);
        String menuIds = requestJson.getString("menuIds");
        menuService.delete(menuIds);
        return ResponseMessage.OK("删除成功");
    }

}
