package com.mit.mission.security.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 *  @autor: gaoy
 *  @date: 2021/4/19 16:40
 *  @description: 用户
 */
@Data
@Entity
@Table(name = "sys_user")
public class User {

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
     * 账号
     */
    @NotEmpty
    @Length(max = 16)
    @Column(columnDefinition = "varchar(16) COMMENT '账号'")
    private String account;

    /**
     * 密码
     */
    @Length(max = 32)
    @Column(columnDefinition = "varchar(32) COMMENT '密码'")
    private String password;

    /**
     * 姓名
     */
    @NotEmpty
    @Length(max = 16)
    @Column(columnDefinition = "varchar(16) COMMENT '姓名'")
    private String name;

    /**
     * 性别（1：男 2：女）
     */
    @Column(columnDefinition = "int(11) COMMENT '性别（1：男 2：女）'")
    private Integer sex;

    /**
     * 电子邮件
     */
    @Column(columnDefinition = "varchar(255) COMMENT '电子邮件'")
    private String email;

    /**
     * 电话
     */
    @Column(columnDefinition = "varchar(255) COMMENT '电话'")
    private String phone;

    /**
     * 角色ids
     */
    @Column(length = 500, columnDefinition = "varchar(500) COMMENT '角色ids'")
    private String roleIds;

    /**
     * 所属叶子部门uuid
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(255) COMMENT '所属叶子部门uuid'")
    private String departId;

    /**
     * 从根部门到叶子部门路径, 分隔符','
     */
    @Column(length = 500, columnDefinition = "varchar(500) COMMENT '从根部门到叶子部门路径, 分隔符逗号'")
    private String departIdPath;

    /**
     * 状态(1：启用 2：冻结 3：删除 4:锁定 5：解锁 )
     */
    @Column(columnDefinition = "int(11) COMMENT '状态(1：启用 2：冻结 3：删除 4:锁定 5：解锁 )'")
    private Integer status;

    /**
     * 密码状态(0：未修改 1：已修改 ）
     */
    @Column(columnDefinition = "int(11) COMMENT '密码状态(0：未修改 1：已修改 ）'")
    private Integer passwordStatus;

    /**
     * 密码过期时间,有限期30天
     */
    @Column(columnDefinition = "datetime COMMENT '密码过期时间,有限期30天'")
    private Date expiredTime;

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
     * 创建用户ID
     */
    @Column(length = 8, columnDefinition = "varchar(32) COMMENT '创建用户姓名'")
    private String createUserName;

    /**
     * 修改用户ID
     */
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT '修改用户ID'")
    private String updateUserId;

    /**
     * 修改用户ID
     */
    @Column(length = 8, columnDefinition = "varchar(32) COMMENT '修改用户姓名'")
    private String updateUserName;
}
