package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.core.domin.SysUser;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
public interface ISysUserService {

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkUserNameUnique(SysUser user);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 验证用户是否存在
     *
     * @param userName
     * @return
     */
    boolean isUserExist(String userName);


    Long getUserId(Long deptId);

}
