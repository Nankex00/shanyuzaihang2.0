package com.fushuhealth.h5.controller;

import com.fushuhealth.h5.core.service.H5SysLoginService;
import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.core.domin.h5.LoginBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhuanz
 * @date 2024/10/11
 */
@RestController
@RequestMapping("/h5")
public class H5AuthorityController {
    @Autowired
    H5SysLoginService loginService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody @Validated LoginBody loginBody, HttpServletRequest request)
    {
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getCode(),
                loginBody.getUuid(),request);
        return AjaxResult.success("登录成功",token);
    }
}
