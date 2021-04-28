package com.mit.mission.core.traffic.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 *  @autor: gaoy
 *  @date: 2021/4/20 16:21
 *  @description: 线路信息
 */
@Entity
@Table(name = "pcc_traffic_line")
@Data
public class Line implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "uuid", length = 32)
    private String uuid;
    /**
     * 线路类型 (1:主线,2:环线,3:支线,4:延长线)
     */
    private Integer type;

    /**
     * 父级ID
     */
    @Column(length = 32)
    private String parentId;

    /**
     * 线路颜色
     */
    private String color;

    /**
     * 线路名称
     */
    @NotEmpty
    private String name;

    /**
     * 英文名称
     */
    private String nameEn;

    /**
     * 线路编号
     */
    private Integer num;

    /**
     * 线路简码
     */
    @NotEmpty
    private String code;

    /**
     * 运营公司
     */
    private String company;

    /**
     * 是否开通(1:是,0:否)
     */
    private Integer isRun;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最近修改时间
     */
    private Date updateTime;

    /**
     * 创建者ID
     */
    @Column(length = 32)
    private String createUserId;

    /**
     * 最近修改者ID
     */
    @Column(length = 32)
    private String updateUserId;
}
