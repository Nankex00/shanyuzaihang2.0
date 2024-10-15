package com.fushuhealth.recovery.h5.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fushuhealth.recovery.common.constant.ScaleStatus;
import com.fushuhealth.recovery.dal.entity.ScaleEvaluationRecord;
import com.fushuhealth.recovery.dal.vo.AbnormalItermInterventionMethodVo;
import com.fushuhealth.recovery.dal.vo.InterventionMethodVo;
import com.fushuhealth.recovery.dal.vo.h5.LoginVo;
import com.fushuhealth.recovery.h5.model.request.CreateScaleEvaluationRecordRequest;

import java.util.List;

public interface ScaleEvaluationRecordService extends IService<ScaleEvaluationRecord> {

    long saveRecord(LoginVo loginVo, CreateScaleEvaluationRecordRequest recordRequest);

    long saveRecordWithOutAnswer(LoginVo loginVo, CreateScaleEvaluationRecordRequest recordRequest);

//    ListScaleEvaluationRecordResponse listRecord(int pageNo, int pageSize, LoginVo user, CategoryType categoryType);
//
//    ScaleTableRecordVo getRecordResult(long recordId);
//
//    ScaleTableRecordVo getRecordResultDetail(long recordId);
//
//    ScaleRecordVo getRecordReport(long recordId);

    int countByUserIdAndScaleCodeAndChildrenIdAndTime(long userId, byte code, long childrenId, long time);

    List<AbnormalItermInterventionMethodVo> getAbnormalMethods(long recordId, String productId);

    InterventionMethodVo getAbnormalMethodDetail(String abnormalIterm);

    List<String> getReportPic(Long id);

    List<ScaleEvaluationRecord> listByChildrenAndScaleTableCodes(long childrenId, List<Byte> scaleTableCodes, List<ScaleStatus> status);

    void saveOrUpdateDDSTRecord(CreateScaleEvaluationRecordRequest recordRequest);
}
