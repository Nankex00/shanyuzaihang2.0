package com.fushuhealth.recovery.h5.controller;

import cn.hutool.core.util.RandomUtil;
import com.fushuhealth.recovery.common.api.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhuanz
 * @date 2024/10/11
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    @GetMapping("/send")
    public AjaxResult send(@RequestParam(required = true) String telephone, HttpServletRequest request){
        HttpSession session = request.getSession();
        //模拟短信验证码生成
        String message = RandomUtil.randomNumbers(6);
        session.setAttribute(telephone,message);
        return AjaxResult.success(message);
    }
}
