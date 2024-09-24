package com.fushuhealth.recovery.device.core.service;

import java.util.HashSet;
import java.util.Set;

import cn.hutool.core.util.ObjectUtil;
import com.fushuhealth.recovery.common.core.domin.SysRole;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.device.service.ISysMenuService;
import com.fushuhealth.recovery.device.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */
@Component
public class SysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            perms.add("*:*:*");
        }
        else
        {
            SysRole role = user.getRole();
            //todo:ObjectUtil的empty与null的区别
            if (ObjectUtil.isNotEmpty(role))
            {
                Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                role.setPermissions(rolePerms);
                perms.addAll(rolePerms);
            }
            else
            {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}

