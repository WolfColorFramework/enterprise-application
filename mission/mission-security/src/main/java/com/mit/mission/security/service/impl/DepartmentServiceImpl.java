package com.mit.mission.security.service.impl;

import java.lang.reflect.Field;
import java.util.*;

import javax.persistence.criteria.Predicate;

import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import com.mit.mission.common.util.AutoMapper;
import com.mit.mission.security.domain.Department;
import com.mit.mission.security.domain.Role;
import com.mit.mission.security.domain.User;
import com.mit.mission.security.dto.DepartmentDto;
import com.mit.mission.security.repository.DepartmentRepository;
import com.mit.mission.security.repository.RoleRepository;
import com.mit.mission.security.repository.UserRepository;
import com.mit.mission.security.service.DepartmentService;
import com.mit.mission.security.util.SecurityKit;
import com.mit.mission.security.util.ToolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public <S extends Department> S add(S entity) {
        build(entity);

        S depart = departmentRepository.save(entity);
        if (entity.getUuid() != null) {
            List<Department> subDeparts = departmentRepository.findAll((Specification<Department>) (root, query, cb) -> {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(cb.like(root.get("parentIds").as(String.class), "%[" + entity.getUuid() + "]%"));
                Predicate[] pre = new Predicate[list.size()];
                return query.where(list.toArray(pre)).getRestriction();
            });
            if (!StringUtils.isEmpty(subDeparts)) {
                for (Department subDepart : subDeparts) {
                    departmentSetPids(subDepart);
                    subDepart.setUpdateTime(new Date());
                    subDepart.setUpdateUserId(SecurityKit.getUser().getUuid());
                    departmentRepository.save(subDepart);
                }
            }
        }
        return depart;
    }

    @Override
    public Boolean update(Department department) {

        Department dataDepartment = this.findOneByUuid(department.getUuid());
        // 部门名称改变了，判断部门名称是否已经存在
        if (!dataDepartment.getName().equals(department.getName())) {
            Department theDepart = this.findOneByName(department.getName());
            if (theDepart != null) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, String.format("[%s]部门已存在", department.getName()));
            }
        }

        // 判断是否将本部门作为上级部门
        if (department.getUuid().equals(department.getParentId())) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, String.format("不能选择自己作为上级部门", department.getName()));
        }

        // 判断是否将本部门作为下级部门
        if (department.getParentId() != null) {
            Department parentDepart = this.findOneByUuid(department.getParentId());
            if (parentDepart != null && parentDepart.getParentIds() != null && parentDepart.getParentIds().contains(department.getUuid())) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, String.format("不能选择自己的下级作为上级部门", department.getName()));
            }
        }

        // 获取同级部门的最大排序号
        Integer maxSort = this.findMaxSort(department.getParentId());
        if (maxSort == null) {
            maxSort = 1;
        } else {
            maxSort++;
        }
        dataDepartment.setSort(maxSort);
        dataDepartment.setName(department.getName());
        dataDepartment.setParentId(department.getParentId());

        // 设置父级菜单编号
        departSetPids(dataDepartment);
        dataDepartment.setUpdateTime(new Date());
        dataDepartment.setUpdateUserId(SecurityKit.getUser().getUuid());
        departmentRepository.save(dataDepartment);

        return true;
    }

    @Override
    public Department findOneByUuid(String uuid) {

        if (StringUtils.isEmpty(uuid)) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR);
        }
        Department department = departmentRepository.getOne(uuid);
        department.setParentIds(department.getParentIds().replace("[", "").replace("]", ""));
        return department;
    }

    @Override
    public Page<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Override
    public void delete(String uuids) {
        String[] ids = uuids.split(",");
        for (String uuid : ids) {
            List<User> users = userRepository.findAllByDepartId(uuid);
            if (!CollectionUtils.isEmpty(users)) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR);
            }
            List<Role> roles = roleRepository.findAllByDepartmentId(uuid);
            if (!CollectionUtils.isEmpty(roles)) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR);
            }
            departmentRepository.deleteById(uuid);
        }
    }

    @Override
    public List<Department> findByUuids(String uuids) {
        List<Department> departments = new ArrayList<>();
        String[] ids = uuids.split(",");
        for (String uuid : ids) {
            departments.add(departmentRepository.getOne(uuid));
        }
        return departments;
    }

    @Override
    public List<Department> findByParentIdsLike(String parentIds) {
        return departmentRepository.findByParentIdsLike(parentIds);
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department findOneByName(String name) {
        return departmentRepository.findOneByName(name);
    }

    @Override
    public List<DepartmentDto> tree() throws Exception {
        List<Department> departments = this.findAll();
        List<DepartmentDto> departmentDtos = AutoMapper.mapperList(departments, DepartmentDto.class);
        departmentDtos = ToolUtils.factorTree(departmentDtos);
        return departmentDtos;
    }

    @Override
    public Integer findMaxSort(String parentId) {
        return departmentRepository.findMaxSort(parentId);
    }

    @Override
    public String findParentIdsByUuid(String uuid) {
        return departmentRepository.findParentIdsByUuid(uuid);
    }

    private void departmentSetPids(Department department) {
        if (StringUtils.isEmpty(department.getParentId()) || "0".equals(department.getParentId())) {
            department.setParentId("0");
            department.setParentIds("[0],");
        } else {
            String pid = department.getParentId();
            Department temp = departmentRepository.findById(pid).get();
            String pids = temp.getParentIds();
            department.setParentId(pid);
            department.setParentIds(pids + "[" + pid + "],");
        }
    }

    // 构造department
    private void build(Department department) {
        // 判断部门名称是否重复
        Department currentDepart = this.findOneByName(department.getName());
        if (currentDepart != null) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, "部门已经存在");
        }

        departSetPids(department);
        // 获取同级部门的最大排序号
        Integer maxSort = this.findMaxSort(department.getParentId());
        if (maxSort == null) {
            maxSort = 1;
        } else {
            maxSort++;
        }
        department.setSort(maxSort);
        department.setCreateTime(new Date());
        department.setUpdateTime(new Date());
        department.setCreateUserId(SecurityKit.getUser().getUuid());
        department.setUpdateUserId(SecurityKit.getUser().getUuid());
    }

    // 设置父级菜单编号
    private void departSetPids(Department department) {
        if (StringUtils.isEmpty(department.getParentId()) || "0".equals(department.getParentId())) {
            department.setParentId("0");
            department.setParentIds("[0],");
        } else {
            String pid = department.getParentId();
            Department temp = this.findOneByUuid(pid);
            String pids = temp.getParentIds();
            department.setParentId(pid);
            department.setParentIds(pids + "[" + pid + "],");
        }
    }
}
