package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.core.domin.SysMenu;
import com.fushuhealth.recovery.device.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private ISysMenuService menuService;

    /**
     * 加载对应角色菜单列表树
     */
//    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
//    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
//    {
//        List<SysMenu> menus = menuService.selectMenuList(getUserId());
//        AjaxResult ajax = AjaxResult.success();
//        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
//        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
//        return ajax;
//    }
}
