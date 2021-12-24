package com.mit.mission.common.properties;

import lombok.Data;

/**
 *  @autor: gaoy
 *  @date: 2021/8/30 10:15
 *  @description: Ftp配置映射
 */
@Data
public class FtpProperties {
    private String ip;
    private String port;
    private String loginName;
    private String password;
}
