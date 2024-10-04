package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.DangerLevelType;
import com.fushuhealth.recovery.common.constant.MonthType;
import com.fushuhealth.recovery.common.core.domin.SysUser;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.SysDeptMapper;
import com.fushuhealth.recovery.dal.dao.SysUserMapper;
import com.fushuhealth.recovery.dal.dao.TransferRecordMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.dal.entity.TransferRecord;
import com.fushuhealth.recovery.device.model.dto.SysDeptNameDto;
import com.fushuhealth.recovery.device.model.dto.TransferRecordListDto;
import com.fushuhealth.recovery.device.model.request.TransferRecordListRequest;
import com.fushuhealth.recovery.device.model.request.TransferRequest;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateListResponse;
import com.fushuhealth.recovery.device.model.response.TransferRecordListResponse;
import com.fushuhealth.recovery.device.model.response.TransferRecordResponse;
import com.fushuhealth.recovery.device.service.ITransferRecordService;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@Service
public class TransferRecordServiceImpl implements ITransferRecordService {
    @Autowired
    private TransferRecordMapper transferRecordMapper;
    @Autowired
    private ChildrenMapper childrenMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public int transferInstitution(TransferRequest request) {
        //修改儿童表
        LambdaUpdateWrapper<Children> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Children::getDeptId,request.getReceiveId())
                .set(Children::getUpdateBy, SecurityUtils.getUserId())
                .eq(Children::getId,request.getChildId());
        childrenMapper.update(new Children(),lambdaUpdateWrapper);
        //新增转诊记录
        TransferRecord transferRecord = new TransferRecord();
        transferRecord.setChildId(request.getChildId());
        transferRecord.setTransferInstitution(request.getTransferId());
        transferRecord.setReceiveInstitution(request.getReceiveId());
        transferRecord.setOperation("转诊");
        transferRecord.setOperatedTime(new Date());
        return transferRecordMapper.insert(transferRecord);
    }

    @Override
    public BaseResponse<List<TransferRecordResponse>> searchTransferRecord(Long childId) {
        LambdaQueryWrapper<TransferRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TransferRecord::getChildId,childId);
        List<TransferRecord> transferRecords = transferRecordMapper.selectList(lambdaQueryWrapper);
        List<TransferRecordResponse> responses = new ArrayList<>();
        transferRecords.forEach(transferRecord -> {
            TransferRecordResponse response = BeanUtil.copyProperties(transferRecord,TransferRecordResponse.class);
            String transferInstitutionName = Optional.ofNullable(sysDeptMapper.selectById(transferRecord.getTransferInstitution())).orElseThrow(() -> {
                throw new ServiceException("数据异常，不存在对应的机构信息");
            }).getDeptName();
            response.setTransferInstitutionName(transferInstitutionName);
            String receiveInstitutionName = Optional.ofNullable(sysDeptMapper.selectById(transferRecord.getReceiveInstitution())).orElseThrow(() -> {
                throw new ServiceException("数据异常，不存在对应的机构信息");
            }).getDeptName();
            response.setReceiveInstitutionName(receiveInstitutionName);
            responses.add(response);
        });
        return new BaseResponse<List<TransferRecordResponse>>(responses, (long) responses.size());
    }

    @Override
    public BaseResponse<List<TransferRecordListResponse>> searchDeptList(TransferRecordListRequest request) {
        Page<TransferRecordListDto> page = new Page<>(request.getPageNum(),request.getPageSize());
        //todo:接诊回复联表查询
        //todo:数据权限能否实现a转b b同样可以看
        MPJLambdaWrapper<Children> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(Children.class)
                .selectAll(TransferRecord.class)
                .leftJoin(TransferRecord.class,TransferRecord::getChildId,Children::getId);
        if (request.getQuery()!=null&& !request.getQuery().isEmpty()){
            lambdaWrapper.like(Children::getId,request.getQuery())
                    .or()
                    .like(Children::getName,request.getQuery());
        }
        Byte type = request.getType() != null ? request.getType() : 0; // 默认值为 0，根据需要修改
        lambdaWrapper.eq(type != 0, Children::getDangerLevel, type);
        lambdaWrapper.orderByDesc(TransferRecord::getOperatedTime);
        childrenMapper.selectJoinPage(page,TransferRecordListDto.class,lambdaWrapper);
        List<TransferRecordListResponse> responses = new ArrayList<>();
        page.getRecords().forEach(transferRecordListDto -> {
            TransferRecordListResponse response = BeanUtil.copyProperties(transferRecordListDto,TransferRecordListResponse.class);
            response.setDangerLevel(DangerLevelType.findDangerLevelByType(transferRecordListDto.getDangerLevel()));
            MPJLambdaWrapper<SysUser> lambdaWrapper1 = new MPJLambdaWrapper<>();
            lambdaWrapper1
                    .selectAs(SysDept::getDeptName, SysDeptNameDto::getName)
                    .eq(SysUser::getUserId,transferRecordListDto.getTransferInstitutionName())
                    .leftJoin(SysDept.class,SysDept::getDeptId,SysUser::getDeptId);
            SysDeptNameDto transferInstitutionName = Optional.ofNullable(sysUserMapper.selectJoinOne(SysDeptNameDto.class, lambdaWrapper1)).orElseThrow(() -> new ServiceException("数据异常，不存在对应的机构信息"));
            response.setTransferInstitutionName(transferInstitutionName.getName());
            MPJLambdaWrapper<SysUser> lambdaWrapper2 = new MPJLambdaWrapper<>();
            lambdaWrapper1
                    .selectAs(SysDept::getDeptName, SysDeptNameDto::getName)
                    .eq(SysUser::getUserId,transferRecordListDto.getReceiveInstitutionName())
                    .leftJoin(SysDept.class,SysDept::getDeptId,SysUser::getDeptId);
            SysDeptNameDto receiveInstitutionName = Optional.ofNullable(sysUserMapper.selectJoinOne(SysDeptNameDto.class, lambdaWrapper1)).orElseThrow(() -> new ServiceException("数据异常，不存在对应的机构信息"));
            response.setReceiveInstitutionName(receiveInstitutionName.getName());
            responses.add(response);
        });
        return new BaseResponse<List<TransferRecordListResponse>>(responses, page.getTotal());
    }
}
