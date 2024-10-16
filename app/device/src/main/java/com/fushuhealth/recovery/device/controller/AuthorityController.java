package com.fushuhealth.recovery.device.controller;

import cn.hutool.core.bean.BeanUtil;
import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.core.domin.LoginBody;
import com.fushuhealth.recovery.common.core.domin.SysMenu;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.device.core.service.SysLoginService;
import com.fushuhealth.recovery.device.core.service.SysPermissionService;
import com.fushuhealth.recovery.device.model.response.InfoResponse;
import com.fushuhealth.recovery.device.model.vo.SysUserVo;
import com.fushuhealth.recovery.device.service.ISysDeptService;
import com.fushuhealth.recovery.device.service.ISysMenuService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class AuthorityController {

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysDeptService sysDeptService;


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody, HttpServletRequest request)
    {
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid(),request);
        return AjaxResult.success("登录成功",token);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();

        SysUserVo sysUserVo = BeanUtil.copyProperties(user, SysUserVo.class);
        sysUserVo.setInstitutionLevel(sysDeptService.selectDeptLevelByDeptId(user.getDeptId()));
        sysUserVo.setDeptName(sysDeptService.selectDeptNameById(SecurityUtils.getLoginUser().getDeptId()));
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);

        Long institutionLevel = sysDeptService.selectDeptLevelByDeptId(user.getDeptId());

        return AjaxResult.success("获取用户信息成功",new InfoResponse(sysUserVo,roles,permissions,institutionLevel));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success("获取路由信息成功",menuService.buildMenus(menus));
    }
}
