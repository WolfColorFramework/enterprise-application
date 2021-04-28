package com.mit.mission.security.controller;

import com.mit.mission.common.web.ResponseMessage;
import com.mit.mission.security.domain.Department;
import com.mit.mission.security.dto.DepartmentDto;
import com.mit.mission.security.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  @autor: gaoy
 *  @date: 2021/4/27 13:59
 *  @description: 部门控制器
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMessage add(@RequestBody Department department) {
        departmentService.add(department);
        return ResponseMessage.OK("保存成功");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseMessage update(@RequestBody Department department) {
        departmentService.update(department);
        return ResponseMessage.OK("修改成功");
    }

    @RequestMapping(value = "/delete/{departmentIds}", method = RequestMethod.POST)
    public ResponseMessage delete(@PathVariable("departmentIds") String departmentIds) {
        departmentService.delete(departmentIds);
        return ResponseMessage.OK("删除成功");
    }

    @RequestMapping(value = "/query/{departmentId}", method = RequestMethod.GET)
    public ResponseMessage findOneById(@PathVariable("departmentId") String departmentId) {
        Department department = departmentService.findOneByUuid(departmentId);
        return ResponseMessage.OK("查询成功", department);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public ResponseMessage getTree() throws Exception {
        List<DepartmentDto> tree = departmentService.tree();
        return ResponseMessage.OK("查询成功", tree);
    }
}
