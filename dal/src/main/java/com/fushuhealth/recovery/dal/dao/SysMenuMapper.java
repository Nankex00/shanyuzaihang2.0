package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fushuhealth.recovery.common.core.domin.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Select("select distinct m.perms " +
            " from sys_menu m" +
            " left join sys_role_menu rm on m.menu_id = rm.menu_id" +
            " where m.status = '0' and rm.role_id = #{roleId}")
    public List<String> selectMenuPermsByRoleId(Long roleId);


    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("select distinct m.perms" +
            " from sys_menu m" +
            " left join sys_role_menu rm on m.menu_id = rm.menu_id" +
            " left join sys_user_role ur on rm.role_id = ur.role_id" +
            " left join sys_role r on r.role_id = ur.role_id" +
            " where m.status = '0' and r.status = '0' and ur.user_id = #{userId}")
    public List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    @Select("select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.`query`, m.visible, m.status, ifnull(m.perms,'') as perms, m.is_frame, m.is_cache, m.menu_type, m.icon, m.order_num, m.create_time" +
            " from sys_menu m where m.menu_type in ('M', 'C') and m.status = 0" +
            " order by m.parent_id, m.order_num")
    public List<SysMenu> selectMenuTreeAll();


    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Select("select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.`query`, m.visible, m.status, ifnull(m.perms,'') as perms, m.is_frame, m.is_cache, m.menu_type, m.icon, m.order_num, m.create_time" +
            " from sys_menu m" +
            " left join sys_role_menu rm on m.menu_id = rm.menu_id" +
            " left join sys_user_role ur on rm.role_id = ur.role_id" +
            " left join sys_role ro on ur.role_id = ro.role_id" +
            " left join sys_user u on ur.user_id = u.user_id" +
            " where u.user_id = #{userId} and m.menu_type in ('M', 'C') and m.status = 0  AND ro.status = 0" +
            " order by m.parent_id, m.order_num")
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    List<SysMenu> selectMenuList(SysMenu menu);

    List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    public List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

}
