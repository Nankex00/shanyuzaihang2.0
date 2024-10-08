package com.fushuhealth.recovery.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fushuhealth.recovery.common.core.domin.SysMenu;
import com.fushuhealth.recovery.dal.vo.NewRouterVo;
import com.fushuhealth.recovery.device.model.vo.TreeSelect;

import java.util.List;
import java.util.Set;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */

public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

//    /**
//     * 构建前端路由所需要的菜单
//     *
//     * @param menus 菜单列表
//     * @return 路由列表
//     */
//    public List<RouterVo> buildMenus(List<SysMenu> menus);
    public List<NewRouterVo> buildMenus(List<SysMenu> menus);


    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(Long userId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);


    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<Long> selectMenuListByRoleId(Long roleId);


    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

}
