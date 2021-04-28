package com.mit.mission.security.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  @autor: gaoy
 *  @date: 2021/4/19 16:39
 *  @description: 角色-菜单
 */
@Entity
@Table(name = "sys_role_menu")
@Data
public class RoleMenu implements Serializable {

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
     * 菜单ID
     */
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT '菜单ID'")
    private String menuId;

    /**
     * 角色ID
     */
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT '角色ID'")
    private String roleId;

}
