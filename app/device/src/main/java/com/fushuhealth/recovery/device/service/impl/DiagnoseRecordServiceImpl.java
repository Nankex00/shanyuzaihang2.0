package com.fushuhealth.recovery.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.dal.dao.DiagnoseMapper;
import com.fushuhealth.recovery.dal.dao.DiagnoseRecordMapper;
import com.fushuhealth.recovery.dal.entity.DiagnoseRecord;
import com.fushuhealth.recovery.device.model.response.DiagnoseRecordResponse;
import com.fushuhealth.recovery.device.service.IDiagnoseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Service
public class DiagnoseRecordServiceImpl implements IDiagnoseRecordService {
    @Autowired
    DiagnoseRecordMapper diagnoseRecordMapper;
    @Autowired
    DiagnoseMapper diagnoseMapper;
    @Override
    public BaseResponse<List<DiagnoseRecordResponse>> searchListByChildId(Long childId) {
        LambdaQueryWrapper<DiagnoseRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DiagnoseRecord::getChildId,childId);
        List<DiagnoseRecord> diagnoseRecords = diagnoseRecordMapper.selectList(lambdaQueryWrapper);
        List<DiagnoseRecordResponse> responses = new ArrayList<>();
        StringJoiner stringJoiner1 = new StringJoiner(",");
        StringJoiner stringJoiner2 = new StringJoiner(",");
        diagnoseRecords.forEach(diagnoseRecord -> {
            DiagnoseRecordResponse response = BeanUtil.copyProperties(diagnoseRecord,DiagnoseRecordResponse.class);
            diagnoseRecord.getBeforeDiagnose().forEach(before->{
                String categoryName = diagnoseMapper.selectById(before).getCategoryName();
                stringJoiner1.add(categoryName);
            });
            diagnoseRecord.getDiagnoseDetail().forEach(detail->{
                String categoryName = diagnoseMapper.selectById(detail).getCategoryName();
                stringJoiner2.add(categoryName);
            });
            response.setBeforeDiagnose(stringJoiner1.toString());
            response.setDiagnoseDetail(stringJoiner2.toString());
            responses.add(response);
        });
        return new BaseResponse<List<DiagnoseRecordResponse>>(responses, (long) responses.size());
    }
}
