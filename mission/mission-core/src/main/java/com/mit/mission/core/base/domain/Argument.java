package com.mit.mission.core.base.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 *  @autor: gaoy
 *  @date: 2021/4/20 15:37
 *  @description: 参数类
 */
@Entity
@Table(name = "pcc_argument")
@Data
public class Argument implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识UUID
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String uuid;

    /**
     * 标题
     */
    private String title;

    /**
     * 名称(编号)
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建用户ID
     */
    private String createUserId;

    /**
     * 修改用户ID
     */
    private String updateUserId;

    /**
     * 0:所有用户可见，1:仅ADMIN可见
     */
    private Integer type;
}
