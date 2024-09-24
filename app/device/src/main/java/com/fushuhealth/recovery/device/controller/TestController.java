package com.fushuhealth.recovery.device.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhuanz
 * @date 2024/9/20
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "success";
    }

}
