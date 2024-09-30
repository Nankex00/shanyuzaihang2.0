package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.common.util.SecurityUtils;
import com.fushuhealth.recovery.dal.dao.ChildrenMapper;
import com.fushuhealth.recovery.dal.dao.SysDeptMapper;
import com.fushuhealth.recovery.dal.dao.TransferRecordMapper;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.dal.entity.TransferRecord;
import com.fushuhealth.recovery.device.model.request.TransferRequest;
import com.fushuhealth.recovery.device.model.response.TransferRecordResponse;
import com.fushuhealth.recovery.device.service.ITransferRecordService;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
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

    @Override
    public int transferInstitution(TransferRequest request) {
        //todo:管理机构不在本机构的，不显示转诊按钮（管理机构已经到接诊机构，则本机构不可以再为同一个儿童多次转诊，
        // 除非接诊机构结案，管理机构变更为本机构，则可以转诊。转诊是管理机构的变更）
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
}
