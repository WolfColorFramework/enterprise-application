package com.mit.mission.common.properties;

import com.mit.mission.common.handler.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *  @autor: gaoy
 *  @date: 2021/8/26 16:32
 *  @description: ftp属性
 */
@Data
@Component
@PropertySource(value = {"classpath:custom.yml"}, factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "ftp-config")
public class FtpManager {
    private FtpProperties ftpMain;
    private FtpProperties ftpRedundancy;
    private String uploadPath;
}
