package com.fushuhealth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.fushuhealth.recovery.dal.dao")
@EnableScheduling
public class H5Application {
    public static void main(String[] args) {
        SpringApplication.run(H5Application.class);
    }
}
