package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.core.domin.dto.RoleDTO;

import java.util.List;
import java.util.Set;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */
public interface ISysRoleService {

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 根据用户id查询角色DTO
     * @param userId
     * @return
     */
    List<RoleDTO> selectRoleDTOByUserId(Long userId);
}
