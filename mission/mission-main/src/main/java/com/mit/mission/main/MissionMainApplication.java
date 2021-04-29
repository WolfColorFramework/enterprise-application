package com.mit.mission.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mit.mission.flowable", "com.mit.mission.common",
        "com.mit.mission.log", "com.mit.mission.security", "com.mit.pis"})
public class MissionMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MissionMainApplication.class, args);
    }
}
