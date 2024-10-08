package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.core.domin.SysMenu;
import com.fushuhealth.recovery.device.model.request.PermissionRequest;
import com.fushuhealth.recovery.device.service.ISysMenuService;
import com.fushuhealth.recovery.device.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fushuhealth.recovery.common.api.AjaxResult.success;
import static com.fushuhealth.recovery.common.util.SecurityUtils.getUserId;

/**
 * @author Zhuanz
 * @date 2024/9/25
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private ISysRoleService roleService;

    /**
     * 修改角色权限（新增角色重新配置）
     */
    @PostMapping("/edit")
    @Operation(summary = "修改角色权限（新增角色重新配置）")
    public AjaxResult edit(@RequestBody @Validated PermissionRequest request){
        return AjaxResult.success(roleService.editUserPermission(request));
    }
}
