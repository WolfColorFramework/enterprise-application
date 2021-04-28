package com.mit.mission.core.traffic.dto;

import com.mit.mission.core.traffic.domain.Station;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *  @autor: gaoy
 *  @date: 2021/4/21 9:39
 *  @description: 线路dto
 */
@Data
public class LineDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private Integer type;

    private String typeName;

    private String color;

    private String name;

    private String nameEn;

    private String parentId;

    private Integer num;

    private String code;

    private String company;

    private Integer isRun;

    private Date createTime;

    private Date updateTime;

    private String createUserName;

    private String updateUserName;

    private List<Station> ups;

    private List<Station> downs;

    private String firstStationName;

    private String lastStationName;
}
