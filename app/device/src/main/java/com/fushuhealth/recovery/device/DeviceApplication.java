package com.fushuhealth.recovery.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.fushuhealth.recovery.dal.dao")
@ComponentScan(basePackages = {"com.fushuhealth.recovery"}) // 替换为您的实际包路径
@EnableScheduling
public class DeviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class);
    }
}
