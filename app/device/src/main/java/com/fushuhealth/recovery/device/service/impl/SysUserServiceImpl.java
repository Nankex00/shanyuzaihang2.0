package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.constant.*;
import com.fushuhealth.recovery.common.core.domin.LoginUser;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.DateUtil;
import com.fushuhealth.recovery.common.util.OldDateUtil;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.common.util.StringUtils;
import com.fushuhealth.recovery.dal.dao.ActionsDao;
import com.fushuhealth.recovery.dal.dao.SysUserMapper;
import com.fushuhealth.recovery.dal.dao.SysUserRoleMapper;
import com.fushuhealth.recovery.dal.dto.DoctorVo;
import com.fushuhealth.recovery.dal.entity.Actions;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.dal.entity.SysUserRole;
import com.fushuhealth.recovery.dal.vo.UserListVo;
import com.fushuhealth.recovery.device.model.response.PatientListResponse;
import com.fushuhealth.recovery.device.model.vo.LoginVo;
import com.fushuhealth.recovery.device.model.vo.PageVo;
import com.fushuhealth.recovery.device.service.ActionService;
import com.fushuhealth.recovery.device.service.ISysUserService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ActionsDao actionsDao;

    @Override
    public SysUser selectUserByUserName(String userName) {
        return sysUserMapper.selectUserByUserName(userName);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = sysUserMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {
        // 新增用户信息
        int rows = sysUserMapper.insert(user);
        user.setUserId(user.getUserId());
        // 新增用户岗位关联
//        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    @Override
    public boolean isUserExist(String userName) {
        SysUser info = sysUserMapper.checkUserNameUnique(userName);
        if (StringUtils.isNotNull(info))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user)
    {
        SysUserRole sysUserRole = BeanUtil.copyProperties(user, SysUserRole.class);
        sysUserRoleMapper.insert(sysUserRole);
    }


    @Override
    public Long getUserId(Long deptId) {
        MPJLambdaWrapper<SysUser> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper
                .selectAll(SysUserRole.class)
                .eq(SysUser::getDeptId,deptId)
                .leftJoin(SysUserRole.class,SysUserRole::getUserId,SysUser::getUserId);
        return Optional.ofNullable(sysUserMapper.selectJoinOne(SysUserRole.class, lambdaWrapper)).orElseThrow(() -> new ServiceException("数据异常,不存在对应的关联表")).getUserId();
    }

    @Override
    public SysUser getUser(long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public LoginVo getLoginVo() {
        //todo:需要完善
        LoginVo loginVo = new LoginVo();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginVo.setId(loginUser.getUserId());
        loginVo.setRoleId(loginUser.getRoleId());
        LambdaQueryWrapper<SysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(SysUserRole::getRoleId,loginUser.getUserId());
        String roleName = sysUserRoleMapper.selectList(lambdaQueryWrapper).stream().map(SysUserRole::getRoleId)
                .anyMatch(roleId -> roleId.equals(9L)) ? OldRoleEnum.ROLE_PATIENT.getName() : OldRoleEnum.ROLE_DOCTOR.getName();
        loginVo.setRoleName(roleName);
        loginVo.setActionIds(actionsDao.selectList(new LambdaQueryWrapper<>()).stream().map(Actions::getId).collect(Collectors.toList()));
        return loginVo;
    }


    private Page<SysUser> queryUser(int pageNo, int pageSize, String word, LoginVo loginVo) {
        Page<SysUser> page = new Page<>(pageNo, pageSize);

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        if (RoleEnum.ROLE_MECHANISM_ADMIN.getName().equalsIgnoreCase(loginVo.getRoleName())) {
//            wrapper.eq("organization_id", loginVo.getOrganizationId());
//        } else if (RoleEnum.ROLE_SUPER_ADMIN.getName().equalsIgnoreCase(loginVo.getRoleName())) {
//
//        } else {
//            wrapper.eq("doctor_id", loginVo.getId());
//        }
        wrapper.eq("organization_id", loginVo.getOrganizationId());
        if (OldRoleEnum.ROLE_DOCTOR.getName().equalsIgnoreCase(loginVo.getRoleName())) {
            wrapper.and(i -> i.eq("doctor_id", loginVo.getId()).or().eq("doctor_id", 0));
        }

        if (!org.apache.commons.lang3.StringUtils.isBlank(word)) {
            wrapper.and(i -> i.like("name", word).or().like("phone", word));
        }
        wrapper.orderByDesc("created");
        return sysUserMapper.selectPage(page, wrapper);
    }
}
