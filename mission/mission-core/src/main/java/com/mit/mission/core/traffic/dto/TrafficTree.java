package com.mit.mission.core.traffic.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *  @autor: gaoy
 *  @date: 2021/4/19 16:40 
 *  @description: 轨交树
 */
@Data
public class TrafficTree implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点id
     */
    private String id;

    /**
     * 节点显示标签
     */
    private String label;

    /**
     * 节点业务类型
     */
    private String type;

    /**
     * 节点存储data
     */
    private Map<String, Object> data;

    /**
     * 子节点树
     */
    private List<TrafficTree> children;

    /**
     * 线路id
     */
    private String lineId;

    /**
     * 车站id
     */
    private String statId;
    /**
     * 唯一标识key
     */
    private String key;
}
