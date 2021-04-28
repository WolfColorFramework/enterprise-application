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
 *  @description: 角色
 */
@Entity
@Table(name = "sys_role")
@Data
public class Role implements Serializable {

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
     * 序号
     */
    @Column(columnDefinition = "int(11) COMMENT '序号'")
    private Integer sort;

    /**
     * 角色名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(255) COMMENT '角色名称'")
    private String name;

    /**
     * 部门ID
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(32) COMMENT '部门ID'")
    private String departmentId;

    /**
     * 备注
     */
    @Column(columnDefinition = "varchar(255) COMMENT '备注'")
    private String remark;

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
