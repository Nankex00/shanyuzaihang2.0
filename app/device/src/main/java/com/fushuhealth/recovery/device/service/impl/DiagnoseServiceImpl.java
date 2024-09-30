package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.ServiceConstants;
import com.fushuhealth.recovery.common.exception.ServiceException;
import com.fushuhealth.recovery.dal.dao.*;
import com.fushuhealth.recovery.dal.entity.*;
import com.fushuhealth.recovery.device.model.request.DiagnoseRequest;
import com.fushuhealth.recovery.device.model.response.DiagnoseResponse;
import com.fushuhealth.recovery.device.service.IDiagnoseService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Service
public class DiagnoseServiceImpl implements IDiagnoseService {
    @Autowired
    private DiagnoseMapper diagnoseMapper;
    @Autowired
    private DangerLevelChangeRecordMapper dangerLevelChangeRecordMapper;
    @Autowired
    private SettleRecordMapper settleRecordMapper;
    @Autowired
    private TransferRecordMapper transferRecordMapper;
    @Autowired
    private ChildrenMapper childrenMapper;
    @Autowired
    private DiagnoseRecordMapper diagnoseRecordMapper;

    @Override
    public BaseResponse<List<DiagnoseResponse>> list() {
        LambdaQueryWrapper<Diagnose> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Diagnose::getCategory,ServiceConstants.DIAGNOSE_FIRST);
        List<Diagnose> diagnoses = diagnoseMapper.selectList(lambdaQueryWrapper);
        List<DiagnoseResponse> responses = new ArrayList<>();
        diagnoses.forEach(diagnose -> {
            DiagnoseResponse diagnoseResponse = BeanUtil.copyProperties(diagnose, DiagnoseResponse.class);
            MPJLambdaWrapper<Diagnose> lambdaQueryWrapper1 = new MPJLambdaWrapper<>();
            lambdaQueryWrapper1
                    .selectAs(Diagnose::getId,DiagnoseResponse::getId)
                    .selectAs(Diagnose::getCategoryName,DiagnoseResponse::getCategoryName)
                    .eq(Diagnose::getParentId,diagnose.getId());
            List<DiagnoseResponse> diagnoses1 = diagnoseMapper.selectJoinList(DiagnoseResponse.class,lambdaQueryWrapper1);
            diagnoseResponse.setChildren(diagnoses1);
            responses.add(diagnoseResponse);
        });
        return new BaseResponse<List<DiagnoseResponse>>(responses, (long) responses.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int settleDiagnose(DiagnoseRequest request) {
        //todo:权限昨晚之后处理这里deptId传入还是通过LoginUser获取的问题
        //对诊断记录进行字符串处理
        StringJoiner joiner = new StringJoiner(",");
        request.getDiagnoses().forEach(diagnose->{
            String categoryName = Optional.ofNullable(diagnoseMapper.selectById(diagnose)).orElseThrow(() -> new ServiceException("数据异常，不存在对应的信息")).getCategoryName();
            joiner.add(categoryName);
        });
        String reason = joiner.toString();
        //高危等级记录变更
        DangerLevelChangeRecord dangerLevelChangeRecord = new DangerLevelChangeRecord();
        dangerLevelChangeRecord.setChildId(request.getChildId());
        dangerLevelChangeRecord.setDangerLevel(request.getDangerLevel());
        dangerLevelChangeRecord.setReason(reason);
        dangerLevelChangeRecord.setOperatedTime(new Date());
        dangerLevelChangeRecord.setOperatedInstitution(request.getDeptId());
        dangerLevelChangeRecordMapper.insert(dangerLevelChangeRecord);
        //诊断记录变更
        DiagnoseRecord diagnoseRecord = new DiagnoseRecord();
        diagnoseRecord.setChildId(request.getChildId());
        diagnoseRecord.setOperatedId(request.getDeptId());
        diagnoseRecord.setOperatedTime(new Date());
        diagnoseRecord.setDiagnoseDetail(request.getDiagnoses());
        //结案记录变更
        SettleRecord settleRecord = new SettleRecord();
        settleRecord.setDangerLevel(request.getDangerLevel());
        settleRecord.setReason(reason);
        settleRecord.setOperatedId(request.getDeptId());
        settleRecord.setChildId(request.getChildId());
        settleRecordMapper.insert(settleRecord);
        //将儿童转回原机构
        LambdaQueryWrapper<TransferRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TransferRecord::getChildId,request.getChildId())
                        .orderByDesc(TransferRecord::getOperatedTime)
                .last("limit 1");
        TransferRecord transferRecord = transferRecordMapper.selectOne(lambdaQueryWrapper);
        LambdaUpdateWrapper<Children> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Children::getDeptId,transferRecord.getTransferInstitution());
        return childrenMapper.update(new Children(),lambdaUpdateWrapper);
    }

    @Override
    public BaseResponse<List<Long>> searchDiagnoseByChildId(Long childId) {
        LambdaQueryWrapper<DiagnoseRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DiagnoseRecord::getChildId,childId)
                .orderByDesc(DiagnoseRecord::getOperatedTime)
                .last("limit 1");
        DiagnoseRecord diagnoseRecord = diagnoseRecordMapper.selectOne(lambdaQueryWrapper);
        return new BaseResponse<List<Long>>(diagnoseRecord.getDiagnoseDetail(), (long) diagnoseRecord.getDiagnoseDetail().size());
    }

    @Override
    public int addDiagnoseRecord(List<Long> diagnoseDetail) {
        DiagnoseRecord diagnoseRecord = new DiagnoseRecord();
//        diagnoseRecord.setChildId();
//        diagnoseRecord.setDiagnoseDetail();
//        diagnoseRecord.set
        return 0;
    }


}
