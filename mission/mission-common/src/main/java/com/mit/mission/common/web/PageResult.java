package com.mit.mission.common.web;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 *  @autor: gaoy
 *  @date: 2021/4/26 11:24 
 *  @description: 分页实体
 */
@Data
public class PageResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<?> rows;

    private Integer size;

    private Long totalSize;

    private Integer totalPage;

    public PageResult() {
    }

    public PageResult(Page<?> pageData) {
        this.rows = pageData.getContent();
        this.size = pageData.getSize();
        this.totalSize = pageData.getTotalElements();
        this.totalPage = pageData.getTotalPages();
    }
}
