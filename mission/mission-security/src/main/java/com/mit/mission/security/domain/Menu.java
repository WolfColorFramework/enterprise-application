package com.mit.mission.security.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 *  @autor: gaoy
 *  @date: 2021/4/19 16:39
 *  @description: 菜单
 */
@Entity
@Table(name = "sys_menu")
@Data
public class Menu implements Serializable {

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
     * 菜单编号
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(255) COMMENT '菜单编号'")
    private String code;

    /**
     * 菜单父ID
     */
    @NotEmpty
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT '菜单父ID'")
    private String parentId;

    /**
     * 当前菜单的所有父菜单ID
     */
    @Column(length = 500, columnDefinition = "varchar(500) COMMENT ' 当前菜单的所有父菜单ID'")
    private String parentIds;

    /**
     * 菜单名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(255) COMMENT '菜单名称'")
    private String name;

    /**
     * 多语言菜单名称
     */
    @Column(columnDefinition = "varchar(255) COMMENT '多语言菜单名称'")
    private String languageName;

    /**
     * 菜单图标
     */
    @Column(columnDefinition = "varchar(255) COMMENT '菜单图标'")
    private String icon;

    /**
     * url地址
     */
    @Column(columnDefinition = "varchar(255) COMMENT 'url地址'")
    private String url;

    /**
     * 菜单排序号
     */
    @Column(columnDefinition = "int(11) COMMENT '菜单排序号'")
    private Integer num;

    /**
     * 菜单层级
     */
    @Column(columnDefinition = "int(11) COMMENT '菜单层级'")
    private Integer level;

    /**
     * 菜单类型(0:菜单 1:按钮2:链接)
     */
    @Column(columnDefinition = "int(11) COMMENT '菜单类型(0:菜单 1:按钮2:链接)'")
    private Integer type;

    /**
     * 备注
     */
    @Column(columnDefinition = "varchar(255) COMMENT '备注'")
    private String remark;

    /**
     * 菜单状态 : 1:启用 2:禁用
     */
    @Column(columnDefinition = "int(11) COMMENT '菜单状态 : 1:启用 2:禁用'")
    private Integer status;

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
