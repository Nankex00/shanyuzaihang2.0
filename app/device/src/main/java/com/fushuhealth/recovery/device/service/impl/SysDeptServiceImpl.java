package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.ReportType;
import com.fushuhealth.recovery.common.constant.ServiceConstants;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.SysDeptMapper;
import com.fushuhealth.recovery.dal.dao.SysUserMapper;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.dal.entity.SysRoleDept;
import com.fushuhealth.recovery.device.model.bo.SysDeptBo;
import com.fushuhealth.recovery.device.model.request.InstitutionRequest;
import com.fushuhealth.recovery.device.model.request.MyDeptRequest;
import com.fushuhealth.recovery.common.constant.Dict;
import com.fushuhealth.recovery.device.model.response.InstitutionResponse;
import com.fushuhealth.recovery.device.model.response.SysDeptResponse;
import com.fushuhealth.recovery.device.service.ISysDeptService;
import com.fushuhealth.recovery.device.service.ISysUserService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
                .eq(ObjectUtil.isNotEmpty(request.getType()),SysDept::getInstitutionLevel,request.getType())
                .leftJoin(SysUser.class,SysUser::getDeptId,SysDept::getDeptId);
        Page<SysDeptResponse> sysDeptPage = sysDeptMapper.selectJoinPage(page, SysDeptResponse.class, lambdaQueryWrapper);
        List<SysDeptResponse> responses = new ArrayList<>();
        sysDeptPage.getRecords().forEach((sysDept)->{
            if (sysDept.getParentId()!=0&&!Objects.equals(sysDept.getDeptId(), SecurityUtils.getLoginUser().getDeptId())){
                SysDeptResponse sysDeptResponse = BeanUtil.copyProperties(sysDept, SysDeptResponse.class);
                String parentDeptName = Optional.ofNullable(sysDeptMapper.selectById(sysDept.getParentId()))
                        .orElseThrow(() -> new ServiceException("没有对应的父机构")).getDeptName();
                sysDeptResponse.setParentDeptName(parentDeptName);
                responses.add(sysDeptResponse);
            }
        });
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
        sysDept.setDeptName(bo.getDeptName());
        //查询对应的父机构数据
        SysDept parentDept = sysDeptMapper.selectById(bo.getParentId());
        if (ObjectUtil.isEmpty(parentDept)){
            throw new ServiceException("没有对应的父机构，操作失败");
        }
        String ancestors = parentDept.getAncestors()+","+bo.getParentId();
        sysDept.setAncestors(ancestors);
        sysDeptMapper.insert(sysDept);
        //新增部门与角色关联,roleId为默认机构等级所对应角色，不能设置为平台管理员
        SysRoleDept roleDept = new SysRoleDept();
        roleDept.setDeptId(sysDept.getDeptId());
        if (bo.getInstitutionLevel()!=1L){
            roleDept.setRoleId(bo.getInstitutionLevel());
        }else {
            throw new ServiceException("参数异常，不允许设置为管理员权限");
        }
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
        if (ObjectUtil.isNull(bo.getDeptId())){
            throw new ServiceException("没有传入id，操作失败");
        }
        SysDept sysDept = sysDeptMapper.selectById(bo.getParentId());
        if (ObjectUtil.isEmpty(sysDept)){
            throw new ServiceException("没有对应的父机构，操作失败");
        }
        String ancestors = sysDept.getAncestors()+","+bo.getParentId();
        //修改机构表
        LambdaUpdateWrapper<SysDept> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysDept::getDeptId,bo.getDeptId())
                .set(SysDept::getDeptName,bo.getDeptName())
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
    @Transactional(rollbackFor = Exception.class)
    public int editMyInstitution(MyDeptRequest bo) {
        SysDept sysDept = sysDeptMapper.selectById(bo.getParentId());
        if (ObjectUtil.isEmpty(sysDept)){
            throw new ServiceException("没有对应的父机构，操作失败");
        }
        String ancestors = sysDept.getAncestors()+","+bo.getParentId();
        //修改机构表
        LambdaUpdateWrapper<SysDept> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysDept::getDeptId,SecurityUtils.getLoginUser().getDeptId())
                .set(SysDept::getDeptName,bo.getDeptName())
                .set(SysDept::getInstitutionLevel,bo.getInstitutionLevel())
                .set(ObjectUtil.isNotEmpty(bo.getAddress()),SysDept::getAddress,bo.getAddress())
                .set(ObjectUtil.isNotEmpty(bo.getDoctor()),SysDept::getDoctor,bo.getDoctor())
                .set(SysDept::getParentId,bo.getParentId())
                .set(ObjectUtil.isNotEmpty(bo.getContactNumber()),SysDept::getContactNumber,bo.getContactNumber())
                .set(SysDept::getAncestors,ancestors);
        sysDeptMapper.update(new SysDept(),lambdaUpdateWrapper);
        //修改用户表
        LambdaUpdateWrapper<SysUser> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper1.eq(SysUser::getUserId,SecurityUtils.getUserId())
                .set(SysUser::getUserName,bo.getUserName())
                .set(SysUser::getPassword,SecurityUtils.encryptPassword(bo.getPassword()));
        return sysUserMapper.update(new SysUser(),lambdaUpdateWrapper1);
    }

    @Override
    public Long getRoleId(Long deptId) {
        MPJLambdaWrapper<SysDept> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper
                .selectAll(SysRoleDept.class)
                .eq(SysDept::getDeptId,deptId)
                .leftJoin(SysRoleDept.class,SysRoleDept::getDeptId,SysDept::getDeptId);
        return Optional.ofNullable(sysDeptMapper.selectJoinOne(SysRoleDept.class, lambdaWrapper)).orElseThrow(() -> new ServiceException("数据异常,不存在对应的关联表")).getRoleId();
    }

    @Override
    public InstitutionResponse searchDetail(Long id) {
        SysUser sysUser = Optional.ofNullable(sysUserMapper.selectById(id)).orElseThrow(()->new ServiceException("参数异常，不存在对应的用户"));
        SysDept sysDept = Optional.ofNullable(sysDeptMapper.selectById(sysUser.getDeptId())).orElseThrow(()->new ServiceException("数据异常，不存在对应的机构"));
        SysDept parentDept = Optional.ofNullable(sysDeptMapper.selectById(sysDept.getParentId())).orElseThrow(()->new ServiceException("数据异常，不存在对应的上级机构"));
        InstitutionResponse institutionResponse = BeanUtil.copyProperties(sysDept, InstitutionResponse.class);
        institutionResponse.setDeptId(id);
        institutionResponse.setUserId(sysUser.getUserId());
        institutionResponse.setUserName(sysUser.getUserName());
        institutionResponse.setParentDeptName(parentDept.getDeptName());
        return institutionResponse;
    }

    @Override
    public String selectDeptNameById(Long institutionId) {
        return Optional.ofNullable(sysDeptMapper.selectById(institutionId)).orElseThrow(() ->
                new ServiceException("数据异常，不存在对应机构")).getDeptName();
    }

    @Override
    public BaseResponse<List<Dict>> searchAncestorsDeptByDeptId(Long deptId) {
        String ancestors = Optional.ofNullable(sysDeptMapper.selectById(deptId)).orElseThrow(()->new ServiceException("参数错误，不存在对应机构信息")).getAncestors();
        String[] split = ancestors.split(",");
        List<Dict> list = new ArrayList<>();
        Arrays.stream(split).forEach((parentId)->{
            if (!parentId.equals(ServiceConstants.DEPT_ROOT_ID)){
                String deptName = Optional.ofNullable(sysDeptMapper.selectById(parentId)).orElseThrow(() -> new ServiceException("数据错误，不存在对应机构信息")).getDeptName();
                list.add(new Dict(Long.parseLong(parentId),deptName));
            }
        });
        return new BaseResponse<List<Dict>>(list, (long) list.size());
    }

    @Override
    public Long selectDeptLevelByDeptId(Long deptId) {
        return Optional.ofNullable(sysDeptMapper.selectById(deptId)).orElseThrow(() -> new ServiceException("数据异常，没有对应的机构数据")).getInstitutionLevel();
    }

    @Override
    public ReportType getReportType(long id) {
        SysDept organization = sysDeptMapper.selectById(id);
        if (organization == null) {
            return ReportType.SIMPLE;
        }
        //todo:默认为简约版吧
//        ReportType reportType = ReportType.getReportType(organization.getReportType());
//        if (reportType == ReportType.UNKNOWN) {
//            reportType = ReportType.SIMPLE;
//        }
        ReportType reportType = ReportType.SIMPLE;
        return reportType;
    }
}
