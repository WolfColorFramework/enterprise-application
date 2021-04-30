package com.mit.mission.security.vo;

public interface MenuVo {
    String getUuid();

    /**
     * 菜单编号
     */
    String getCode();

    /**
     * 菜单父ID
     */
    String getParentId();

    /**
     * 菜单名称
     */
    String getName();

    /**
     * 多语言菜单名称
     */
    String getLanguageName();

    /**
     * 菜单图标
     */
    String getIcon();

    /**
     * url地址
     */
    String getUrl();

    /**
     * 菜单排序号
     */
    Integer getNum();

    /**
     * 菜单层级
     */
    Integer getLevel();

    /**
     * 菜单类型(0:菜单 1:按钮2:链接)
     */
    Integer getType();
}
