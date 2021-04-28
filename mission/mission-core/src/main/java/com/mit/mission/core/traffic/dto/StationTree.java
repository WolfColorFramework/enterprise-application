package com.mit.mission.core.traffic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *  @autor: gaoy
 *  @date: 2021/4/20 16:50
 *  @description: 站台树
 */
@Data
public class StationTree implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String label;

    private Integer type = 2;
}
