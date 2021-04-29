package com.mit.mission.common.properties;

import com.mit.mission.common.handler.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *  @autor: gaoy
 *  @date: 2021/4/29 10:27
 *  @description: 常量配置
 */
@Data
@Component
@PropertySource(value = {"classpath:custom.yml"}, factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "common-const")
public class ConstProperties {
    private String pccCity;
    // 线路方向上行
    private String directionUp;
    // 管理员id
    private String adminId;
    // 超级管理员角色id
    private String adminRoleId;
}
