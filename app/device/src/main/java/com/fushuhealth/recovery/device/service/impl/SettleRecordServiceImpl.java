package com.fushuhealth.recovery.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.dal.dao.SettleRecordMapper;
import com.fushuhealth.recovery.dal.entity.SettleRecord;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.response.SettleRecordResponse;
import com.fushuhealth.recovery.device.service.ISettleRecordService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Service
public class SettleRecordServiceImpl implements ISettleRecordService {
    @Autowired
    private SettleRecordMapper settleRecordMapper;

    @Override
    public BaseResponse<List<SettleRecordResponse>> list(Long childId) {
        MPJLambdaWrapper<SettleRecord> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(SettleRecord.class)
                .selectAs(SysDept::getDeptName,SettleRecordResponse::getOperatedDept)
                .eq(SettleRecord::getChildId,childId)
                .leftJoin(SysDept.class,SysDept::getDeptId,SettleRecord::getOperatedId);
        List<SettleRecordResponse> settleRecordResponses = settleRecordMapper.selectJoinList(SettleRecordResponse.class, lambdaWrapper);
        return new BaseResponse<List<SettleRecordResponse>>(settleRecordResponses, (long) settleRecordResponses.size());
    }
}
