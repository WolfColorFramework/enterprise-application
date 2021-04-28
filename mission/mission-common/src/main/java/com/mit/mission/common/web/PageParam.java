package com.mit.mission.common.web;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Map;

/**
 * @autor: gaoy
 * @date: 2021/4/26 10:53
 * @description: 分页查询实体
 */
@Data
public class PageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page;

    private Integer size;

    private String properties;

    private String direction;

    private Map<String, Object> condition;

    public Pageable formatPageable() {
        return PageRequest.of(this.page - 1, this.size, this.formatSort());
    }

    private Sort.Direction formatDirection() {
        return PageDirection.ASC.equalsIgnoreCase(this.direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    private Sort formatSort() {
        return Sort.by(this.formatDirection(), this.properties);
    }
}
