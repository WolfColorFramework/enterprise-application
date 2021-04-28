package com.mit.mission.security.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 *  @autor: gaoy
 *  @date: 2021/4/19 16:39
 *  @description: 部门
 */
@Entity
@Table(name = "sys_department")
@Data
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT '主键,唯一标识UUID'")
    private String uuid;

    /**
     * 排序
     */
    @Column(columnDefinition = "int(11) COMMENT '排序'")
    private Integer sort;

    /**
     * 父部门ID
     */
    @NotEmpty
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT '父部门ID'")
    private String parentId;

    /**
     * 所有的父部门ID
     */
    @Column(length = 500, columnDefinition = "varchar(500) COMMENT '所有的父部门ID'")
    private String parentIds;

    /**
     * 部门名称
     */
    @NotEmpty
    @Length(max = 16)
    @Column(length = 16, columnDefinition = "varchar(16) COMMENT '部门名称'")
    private String name;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime COMMENT '创建时间'")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(columnDefinition = "datetime COMMENT '修改时间'")
    private Date updateTime;

    /**
     * 创建用户ID
     */
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT '创建用户ID'")
    private String createUserId;

    /**
     * 修改用户ID
     */
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT '修改用户ID'")
    private String updateUserId;
}
