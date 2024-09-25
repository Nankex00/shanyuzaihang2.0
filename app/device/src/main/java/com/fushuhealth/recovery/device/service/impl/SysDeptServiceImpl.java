package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.SysDeptMapper;
import com.fushuhealth.recovery.dal.dao.SysUserMapper;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.bo.SysDeptBo;
import com.fushuhealth.recovery.device.model.request.InstitutionRequest;
import com.fushuhealth.recovery.device.model.response.InstitutionResponse;
import com.fushuhealth.recovery.device.service.ISysDeptService;
import com.fushuhealth.recovery.device.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialException;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysUserMapper sysUserMapper;

    Snowflake snowflake = new Snowflake();

    @Override
    public List<SysDept> list(InstitutionRequest request) {
        Page<SysDept> page = new Page<>(request.getPageNum(),request.getPageSize());
        LambdaQueryWrapper<SysDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(ObjectUtil.isNotEmpty(request.getInstitutionName()),SysDept::getName,request.getInstitutionName())
                .eq(ObjectUtil.isNotEmpty(request.getLevel()),SysDept::getInstitutionLevel,request.getLevel());

        return sysDeptMapper.selectPage(page,lambdaQueryWrapper).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createDept(SysDeptBo bo) {
        SysUser user = BeanUtil.copyProperties(bo,SysUser.class);
        //检查用户账号是否存在
        if (!userService.checkUserNameUnique(user)) {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        //todo:检查用户机构名是否存在

        user.setCreateBy(Long.valueOf(user.getUserName()));
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUserId(snowflake.nextId());
        SysDept sysDept = BeanUtil.copyProperties(bo,SysDept.class);
        sysDept.setDeptId(snowflake.nextId());

        //查询对应的父机构数据
        SysDept parentDept = sysDeptMapper.selectById(bo.getParentId());
        if (ObjectUtil.isEmpty(parentDept)){
            throw new ServiceException("没有对应的父机构，操作失败");
        }
        String ancestors = parentDept.getAncestors()+","+bo.getParentId();
        sysDept.setAncestors(ancestors);

        user.setDeptId(sysDept.getDeptId());
        user.setRoleId(bo.getInstitutionLevel());
        userService.insertUser(user);
        return sysDeptMapper.insert(sysDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDept(Long id,Long userId) {
        //逻辑删除
        LambdaUpdateWrapper<SysDept> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysDept::getDeptId,id)
                .set(SysDept::getDelFlag,1);
        LambdaUpdateWrapper<SysUser> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper1.eq(SysUser::getUserId,userId)
                .set(SysUser::getDelFlag,1);
        sysUserMapper.update(new SysUser(),lambdaUpdateWrapper1);
        return sysDeptMapper.update(new SysDept(),lambdaUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDept(SysDeptBo bo) {
        if (ObjectUtil.isNull(bo.getId())){
            throw new ServiceException("没有传入id，操作失败");
        }
        SysDept sysDept = sysDeptMapper.selectById(bo.getParentId());
        if (ObjectUtil.isEmpty(sysDept)){
            throw new ServiceException("没有对应的父机构，操作失败");
        }
        String ancestors = sysDept.getAncestors()+","+bo.getParentId();
        //修改机构表
        LambdaUpdateWrapper<SysDept> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysDept::getDeptId,bo.getId())
                .set(SysDept::getName,bo.getName())
                .set(SysDept::getInstitutionLevel,bo.getInstitutionLevel())
                .set(ObjectUtil.isNotEmpty(bo.getAddress()),SysDept::getAddress,bo.getAddress())
                .set(ObjectUtil.isNotEmpty(bo.getDoctor()),SysDept::getDoctor,bo.getDoctor())
                .set(SysDept::getParentId,bo.getParentId())
                .set(ObjectUtil.isNotEmpty(bo.getContactNumber()),SysDept::getContactNumber,bo.getContactNumber())
                .set(SysDept::getAncestors,ancestors);
        sysDeptMapper.update(new SysDept(),lambdaUpdateWrapper);
        //修改用户表

        LambdaUpdateWrapper<SysUser> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper1.eq(SysUser::getUserId,bo.getUserId())
                .set(SysUser::getUserName,bo.getUserName())
                .set(SysUser::getPassword,SecurityUtils.encryptPassword(bo.getPassword()));
        return sysUserMapper.update(new SysUser(),lambdaUpdateWrapper1);
    }

    @Override
    public InstitutionResponse searchDetail(Long id) {
        SysDept sysDept = sysDeptMapper.selectById(id);
        InstitutionResponse institutionResponse = BeanUtil.copyProperties(sysDept, InstitutionResponse.class);
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getDeptId,id);
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
        institutionResponse.setUserId(sysUser.getUserId());
        institutionResponse.setName(sysUser.getUserName());
        institutionResponse.setPassword(sysUser.getPassword());
        return institutionResponse;
    }


}
