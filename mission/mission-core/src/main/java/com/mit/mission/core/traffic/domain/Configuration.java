package com.mit.mission.core.traffic.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  @autor: gaoy
 *  @date: 2021/4/21 9:45 
 *  @description: 线路车站配置信息
 */
@Entity
@Table(name = "pcc_traffic_configuration")
@Data
public class Configuration implements Serializable {

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
     * 线路ID
     */
    @Column(length = 32)
    private String lineId;
    /**
     * 车站ID
     */
    @Column(length = 32)
    private String stationId;
    /**
     * 车站编号
     */
    @Column(length = 11)
    private String num;
    /**
     * 方向
     */
    @Column(length = 11)
    private String direction;
    /**
     * 首班时间
     */
    @Column(length = 20)
    private String startTime;
    /**
     * 末班时间
     */
    @Column(length = 20)
    private String endTime;
    /**
     * 是否开通(1:是,0:否)
     */
    private Integer isRun;
    /**
     * 排序序号
     */
    private Integer sort;
}
