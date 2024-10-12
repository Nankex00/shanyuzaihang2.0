package com.fushuhealth.recovery.device.service;


import com.fushuhealth.recovery.dal.entity.TrainingActionRecord;

import java.util.List;

public interface TrainingActionRecordService {

//    ListTrainingActionRecordResponse listTrainingActionRecord(long userId, String word, int pageNo, int pageSize);
//
//    List<TrainingRecordVideoVo> listTrainingRecordVideoVo(long recordId);

    List<TrainingActionRecord> listTrainingActionRecordByRecordId(long recordId);

//    TrainingActionRecord getById(long id);
//
//    void update(TrainingActionRecord trainingActionRecord);
//
//    void saveTrainingAction(TrainingActionRecord trainingActionRecord);
//
//    List<TrainingActionRecordListVoV2> listByPlanIdAndDay(long planId, long patientId, long day);
//
//    List<TrainingActionRecordListVoV2> listByRecordId(TrainingRecord record);
}
