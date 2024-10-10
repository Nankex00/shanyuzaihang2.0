package com.fushuhealth.recovery.device.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fushuhealth.recovery.dal.entity.ScaleEvaluationRecord;
import com.fushuhealth.recovery.dal.vo.ScaleRecordVo;

public interface ScaleRecordService extends IService<ScaleEvaluationRecord> {

//    ListScaleRecordResponse queryScaleRecord(int pageNo, int pageSize, LoginVo loginVo, ScaleRecordQuery query);
//
//    ListScaleRecordResponse queryScaleRecord(int pageNo, int pageSize, Long childrenId);

    ScaleRecordVo getScaleRecordVo(long id, byte type, long fileId);

//    ScaleEvaluationRecord getScaleEvaluationRecord(long id);
//
//    void updateScaleAnswerScore(LoginVo user, UpdateScaleRecordAnswerScoreRequest request);
//
//    void updateScaleRemark(LoginVo user, String request);
//
//    String previewReport(Long id);
//
//    List<String> getReportPic(Long id);
//
//    void saveScaleRecord(ScaleEvaluationRecord record);
//
//    ListClinicEvaluateResponse listClinicEvaluate(int pageNo, int pageSize, LoginVo loginVo, String keyWord, List<Byte> scaleCodes);
//
//    ListScaleOrderResponse listReviewScaleOrder(int pageNo, int pageSize);
//
//    void reviewScaleOrder(ReviewScaleOrderRequest request);
//
//    void cancelClinicScaleTableEvaluation(long id);
//
//    void updateEvaluationReserveTime(long id, long time);
//
//    void updateScoringBook(long id, List<AnswerRequest> answers);
//
//    List<AnswerRequest> getScoringBook(long id);
//
//    void reviewScaleReport(ReviewScaleReportRequest request);
//
//    void sendReport(long id);
//
//    void submitReview(long id);
//
//    ListClinicEvaluateResponse listClinicEvaluate(int pageNo, int pageSize, ScaleStatus scaleStatus, byte orderBy);
//
//    void updateClinicSchedule(UpdateClinicScheduleRequest request);
//
//    void cancelClinicSchedule(long recordId);
//
//    void refund(long recordId);
//
//    List<ClinicScheduleDayVo> listClinicSchedule(String startDay, String endDay);
//
//    ClinicRecordVo getClinicDetail(long id);
//
//    ListRecoveryGuideResponse listRecoveryGuide(int pageNo, int pageSize);
//
//    ListScanCodeRegistrationResponse listScanCodeRegistration(int pageNo, int pageSize);
//
//    ScanCodeRegistrationVo getScanCodeRegistration(long id);
//
//    void updateScanCodeRegistration(UpdateScanCodeRegistrationRequest request);
//
//    void submitScanCodeRegistration(SubmitScanCodeRegistrationRequest request);
//
//    List<ScaleEvaluationRecord> listByChildrenId(long childrenId);
}
