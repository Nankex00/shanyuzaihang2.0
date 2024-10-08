package com.fushuhealth.recovery.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.core.domin.dto.RoleDTO;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.common.util.StringUtils;
import com.fushuhealth.recovery.dal.dao.*;
import com.fushuhealth.recovery.common.core.domin.SysRole;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.dal.entity.SysRoleDept;
import com.fushuhealth.recovery.dal.entity.SysRoleMenu;
import com.fushuhealth.recovery.dal.entity.SysUserRole;
import com.fushuhealth.recovery.device.model.request.PermissionRequest;
import com.fushuhealth.recovery.device.service.ISysRoleDeptService;
import com.fushuhealth.recovery.device.service.ISysRoleService;
import com.fushuhealth.recovery.device.service.ISysUserService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editUserPermission(PermissionRequest request) {
        //插入新的自定义角色
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getRoleKey,request.getDeptId());
        SysRole sysRole = new SysRole();
        SysDept sysDept = sysDeptMapper.selectById(request.getDeptId());
        sysRole.setRoleName(sysDept.getDeptName());
        sysRole.setRoleKey(String.valueOf(sysDept.getDeptId()));
        sysRole.setRoleSort(Math.toIntExact(sysDept.getDeptId()));
        sysRole.setDataScope(request.getDataScope());
        sysRole.setCreateBy(SecurityUtils.getUserId());
        Optional.ofNullable(roleMapper.selectOne(lambdaQueryWrapper)).ifPresentOrElse((role)->{
            sysRole.setRoleId(role.getRoleId());
            roleMapper.updateById(sysRole);
        },()->{
            roleMapper.insert(sysRole);
        });
        //修改SysRoleDept
        LambdaQueryWrapper<SysRoleDept> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(SysRoleDept::getDeptId,request.getDeptId());
        Optional.ofNullable(sysRoleDeptMapper.selectOne(lambdaQueryWrapper1)).ifPresentOrElse((sysRoleDept1)->{
            LambdaUpdateWrapper<SysRoleDept> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(SysRoleDept::getRoleId,sysRole.getRoleId());
            sysRoleDeptMapper.update(lambdaUpdateWrapper);
        },()->{
//            SysRoleDept sysRoleDept = new SysRoleDept();
//            sysRoleDept.setRoleId(sysRole.getRoleId());
//            sysRoleDept.setDeptId(request.getDeptId());
//            sysRoleDeptMapper.insert(sysRoleDept);
            throw new ServiceException("数据异常,不存在对应的关联表");
        });
        //修改SysUserRole
        LambdaQueryWrapper<SysUserRole> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        Long userId = userService.getUserId(request.getDeptId());
        lambdaQueryWrapper2.eq(SysUserRole::getUserId,userId);
        Optional.ofNullable(sysUserRoleMapper.selectOne(lambdaQueryWrapper2)).ifPresentOrElse((sysUserRole)->{
            LambdaUpdateWrapper<SysUserRole> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(SysUserRole::getRoleId,sysRole.getRoleId());
            sysUserRoleMapper.update(lambdaUpdateWrapper);
        },()->{
//            SysUserRole sysUserRole = new SysUserRole();
//            sysUserRole.setRoleId(sysRole.getRoleId());
//            sysUserRole.setUserId(userId);
//            sysUserRoleMapper.insert(sysUserRole);
            throw new ServiceException("数据异常,不存在对应的关联表");
        });
        //配置菜单项
        LambdaQueryWrapper<SysRoleMenu> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper3.eq(SysRoleMenu::getRoleId,sysRole.getRoleId());
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(lambdaQueryWrapper3);
        if (!sysRoleMenus.isEmpty()){
            sysRoleMenuMapper.delete(lambdaQueryWrapper3);
        }
        request.getMenuList().forEach(id->{
            sysRoleMenuMapper.insert(new SysRoleMenu(sysRole.getRoleId(),id));
        });
        return 1;
    }
}
