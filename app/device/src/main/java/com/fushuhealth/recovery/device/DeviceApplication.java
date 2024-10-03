package com.fushuhealth.recovery.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.fushuhealth.recovery.dal.dao")
@ComponentScan(basePackages = {"com.fushuhealth"}) // 替换为您的实际包路径
public class DeviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class);
    }
}
