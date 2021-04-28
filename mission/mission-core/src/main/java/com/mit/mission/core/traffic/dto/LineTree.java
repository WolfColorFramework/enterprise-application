package com.mit.mission.core.traffic.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  @autor: gaoy
 *  @date: 2021/4/20 16:49
 *  @description: 线路树
 */
@Data
public class LineTree implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String label;

    private Integer type = 1;

    private List<StationTree> children;
}
