package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.core.domin.SysMenu;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.entity.SysRoleDept;
import com.fushuhealth.recovery.device.model.vo.SysMenuVo;
import com.fushuhealth.recovery.device.service.ISysDeptService;
import com.fushuhealth.recovery.device.service.ISysMenuService;
import com.fushuhealth.recovery.device.service.ISysRoleService;
import com.fushuhealth.recovery.device.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/10/8
 */
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysUserService userService;

    /**
     * 加载机构对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{deptId}")
    @Operation(summary = "加载机构对应角色菜单列表树")
    public AjaxResult roleMenuTreeselect(@PathVariable("deptId") Long deptId)
    {
        Long roleId = deptService.getRoleId(deptId);
        Long userId = userService.getUserId(deptId);
        List<SysMenu> menus = menuService.selectMenuList(userId);
        SysMenuVo sysMenuVo = new SysMenuVo();
        sysMenuVo.setMenus(menuService.buildMenuTreeSelect(menus));
        sysMenuVo.setCheckedKeys(menuService.selectMenuListByRoleId(roleId));
        String dataScope = roleService.getById(roleId).getDataScope();
        sysMenuVo.setDataScope(Byte.valueOf(dataScope));
        return AjaxResult.success(new BaseResponse<SysMenuVo>(sysMenuVo,1L));
    }
}
