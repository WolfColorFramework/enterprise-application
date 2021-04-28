package com.mit.mission.core.traffic.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 *  @autor: gaoy
 *  @date: 2021/4/20 16:54
 *  @description: 站台
 */
@Entity
@Table(name = "pcc_traffic_station")
@Data
public class Station implements Serializable {

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
     * 车站名称
     */
    @NotEmpty
    private String name;
    /**
     * 车站英文名称
     */
    private String nameEn;

    /**
     * 车站编号
     */
    private String code;
    /**
     * 车站名称简拼
     */
    private String nameSpell;
    /**
     * 胶囊编码
     */
    private String capsules;
    /**
     * 类型：0=STATION，1=STOP
     */
    private Integer type = 0;
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
