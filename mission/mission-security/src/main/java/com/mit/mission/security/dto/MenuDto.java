package com.mit.mission.security.dto;

import com.mit.mission.security.domain.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *  @autor: gaoy
 *  @date: 2021/4/27 17:29
 *  @description: 菜单Dto
 */
@Data
public class MenuDto extends Menu {
    private static final long serialVersionUID = 1L;

    private String[] parentIdList;
    private List<MenuDto> linkedList = new ArrayList<>();
    private List<MenuDto> children;
    private String label;
    private String value;
}
