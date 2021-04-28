package com.mit.mission.security.properties;

import com.mit.mission.common.handler.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = {"classpath:custom.yml"}, factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "security-starting")
public class SecurityProperties {
    private String defaultPWD;
    private Boolean adminOpen;
    private Integer passwordDay;
    private String loginUrl;
    private String logoutUrl;
    private String[] freeUrls;
    private String[] staticUrls;
}
