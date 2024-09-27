package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.IdUtils;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.SysDeptMapper;
import com.fushuhealth.recovery.dal.dao.SysUserMapper;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.bo.SysDeptBo;
import com.fushuhealth.recovery.device.model.request.InstitutionRequest;
import com.fushuhealth.recovery.device.model.response.InstitutionResponse;
import com.fushuhealth.recovery.device.model.response.SysDeptResponse;
import com.fushuhealth.recovery.device.service.ISysDeptService;
import com.fushuhealth.recovery.device.service.ISysUserService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    @Override
    public BaseResponse<List<SysDeptResponse>> list(InstitutionRequest request) {
        Page<SysDeptResponse> page = new Page<>(request.getPageNum(),request.getPageSize());
        MPJLambdaWrapper<SysDept> lambdaQueryWrapper = new MPJLambdaWrapper<>();
        lambdaQueryWrapper.selectAll(SysDept.class)
                .selectAs(SysUser::getUserId,SysDeptResponse::getUserId)
                .like(ObjectUtil.isNotEmpty(request.getInstitutionName()),SysDept::getDeptName,request.getInstitutionName())
                .eq(ObjectUtil.isNotEmpty(request.getLevel()),SysDept::getInstitutionLevel,request.getLevel())
                .leftJoin(SysUser.class,SysUser::getDeptId,SysDept::getDeptId);
        Page<SysDeptResponse> sysDeptPage = sysDeptMapper.selectJoinPage(page, SysDeptResponse.class, lambdaQueryWrapper);
        List<SysDeptResponse> responses = new ArrayList<>();
        sysDeptPage.getRecords().forEach((sysDept)->{
            if (sysDept.getParentId()!=0){
                SysDeptResponse sysDeptResponse = BeanUtil.copyProperties(sysDept, SysDeptResponse.class);
                String parentDeptName = Optional.ofNullable(sysDeptMapper.selectById(sysDept.getParentId()))
                        .orElseThrow(() -> new ServiceException("没有对应的父机构")).getDeptName();
                sysDeptResponse.setParentDeptName(parentDeptName);
                responses.add(sysDeptResponse);
            }
        });
        //todo:分页有bug
        return new BaseResponse<>(responses,sysDeptPage.getTotal());
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

        user.setCreateBy(SecurityUtils.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        SysDept sysDept = BeanUtil.copyProperties(bo,SysDept.class);
        sysDept.setDeptName(bo.getName());
        //查询对应的父机构数据
        SysDept parentDept = sysDeptMapper.selectById(bo.getParentId());
        if (ObjectUtil.isEmpty(parentDept)){
            throw new ServiceException("没有对应的父机构，操作失败");
        }
        String ancestors = parentDept.getAncestors()+","+bo.getParentId();
        sysDept.setAncestors(ancestors);
        sysDeptMapper.insert(sysDept);
        user.setDeptId(sysDept.getDeptId());
        user.setRoleId(bo.getInstitutionLevel());
        return userService.insertUser(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDept(Long userId) {
        //逻辑删除
        LambdaUpdateWrapper<SysUser> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper1.eq(SysUser::getUserId,userId)
                .set(SysUser::getDelFlag,1);
        SysUser sysUser = sysUserMapper.selectById(userId);
        LambdaUpdateWrapper<SysDept> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysDept::getDeptId,sysUser.getDeptId())
                .set(SysDept::getDelFlag,1);
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
                .set(SysDept::getDeptName,bo.getName())
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
        //todo:限制只有一个账号对应一个机构
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
        institutionResponse.setDeptId(id);
        institutionResponse.setUserId(sysUser.getUserId());
        institutionResponse.setUserName(sysUser.getUserName());
        return institutionResponse;
    }


}
