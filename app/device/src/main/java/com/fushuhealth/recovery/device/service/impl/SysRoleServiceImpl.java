package com.fushuhealth.recovery.device.service.impl;

import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.core.domin.dto.RoleDTO;
import com.fushuhealth.recovery.common.util.StringUtils;
import com.fushuhealth.recovery.dal.dao.SysRoleMapper;
import com.fushuhealth.recovery.common.core.domin.SysRole;
import com.fushuhealth.recovery.dal.entity.SysUserRole;
import com.fushuhealth.recovery.device.service.ISysRoleService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */

@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId)
    {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<RoleDTO> selectRoleDTOByUserId(Long userId) {
        MPJLambdaWrapper<SysRole> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(SysRole.class)
                .eq(SysUserRole::getUserId,userId)
                .leftJoin(SysUserRole.class,SysUserRole::getRoleId,SysRole::getRoleId);
        return roleMapper.selectJoinList(RoleDTO.class, lambdaWrapper);
    }
}
