package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fushuhealth.recovery.common.core.domin.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Select("select distinct r.role_id, r.role_name, r.role_key, r.role_sort, r.data_scope, r.menu_check_strictly, r.dept_check_strictly," +
            "    r.status, r.del_flag, r.create_by, r.create_time, r.update_by, r.update_time, r.remark" +
            " from sys_role r" +
            "    left join sys_user_role ur on ur.role_id = r.role_id" +
            "    left join sys_user u on u.user_id = ur.user_id" +
            "    left join sys_dept d on u.dept_id = d.dept_id" +
            " where r.del_flag = '0' and ur.user_id = #{userId}")
    public List<SysRole> selectRolePermissionByUserId(Long userId);

}
