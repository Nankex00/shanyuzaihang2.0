package com.fushuhealth.recovery.device.controller;

import cn.hutool.core.lang.Snowflake;
import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseController;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.common.util.StringUtils;
import com.fushuhealth.recovery.device.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
@RestController
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    Snowflake snowflake = new Snowflake();

    @PostMapping("/sysUser/add")
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (!userService.checkUserNameUnique(user))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
//        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
//        {
//            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
//        }
        user.setCreateBy(Long.valueOf(user.getUserName()));
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUserId(snowflake.nextId());
        return toAjax(userService.insertUser(user));

    }
}
