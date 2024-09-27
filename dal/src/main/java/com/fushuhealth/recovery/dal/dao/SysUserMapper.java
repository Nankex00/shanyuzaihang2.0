package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT  user_id,user_name,dept_id,password,salt_key,enable,status,create_by,create_time,update_by,update_time,del_flag  FROM sys_user WHERE (user_name = #{userName})")
    public SysUser selectUserByUserName(String userName);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Select("select user_id, user_name from sys_user where user_name = #{userName} and del_flag = '0' limit 1")
    public SysUser checkUserNameUnique(String userName);

//    /**
//     * 新增用户信息
//     *
//     * @param user 用户信息
//     * @return 结果
//     */
//    @Insert("INSERT INTO sys_user(user_id,dept_id, user_name, password, create_by) VALUES (#{userId},#{deptId}, #{userName}, #{password},#{createBy})")
//    public  int insertUser(SysUser user);
}
