package com.fushuhealth.recovery.device.service;


import com.fushuhealth.recovery.dal.vo.TrainingRecordVo;

import java.util.List;

public interface TrainingRecordService {

    TrainingRecordVo getTrainingRecordVo(long id);

//    ListTrainingRecordResponse listTrainingRecord(int pageNo, int pageSize, long patientId, byte trainingStatus, String planName);
//
//    List<TrainingRecord> listTrainingRecordByPlanId(long planId, long day);
//
//    void saveTrainingRecord(SaveTrainingRecordRequest request, LoginVo loginVo);
//
//    void createTrainingRecord(CreateTrainingRecordRequest request, LoginVo loginVo);
//
//    List<TrainingRecord> listByPlanIdAndDay(long planId, long patientId, long day);
//
//    List<TrainingRecordListByDayVo> getTrainingRecordList(TrainingRecordQuery query);
//
//    void updateTrainingRecordRemark(UpdateTrainingRecordRemarkRequest request);
//
//    ShareRecordResultVo shareRecordResult(long id);
//
//    int countByPlanId(long planId);
//
//    ListMechanismTrainingRecordResponse listMechanismRecord(int pageNo, int pageSize, long patientId);
//
//    void deleteRecord(long id);
//
//    void updateTrainingRecordAction(long id, long actionId);
}
