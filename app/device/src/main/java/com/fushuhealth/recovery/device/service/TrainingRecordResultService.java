package com.fushuhealth.recovery.device.service;


import com.fushuhealth.recovery.dal.vo.TrainingActionRecordResultVo;
import com.fushuhealth.recovery.dal.vo.TrainingResultVo;

import java.util.List;

public interface TrainingRecordResultService {

//    void resolveGaitAnalysisResult(GaitAnalysisResultNotifyRequest request);
//    //
//    void resolveResult(TrainingActionRecord trainingActionRecord, GaitAnalysisResultNotifyRequest request);
//
//    void resolveScaleResult(GaitAnalysisResultNotifyRequest request);
//    //
//    TrainingResultVo getResult(long id);
//
//    TrainingResultVo getMockResult(long id);

    TrainingResultVo getScaleRecordResult(long id);

//    void updateResult(UpdateRecordResultRequest request);
//
//    TrainingRecordResultPlanVo getResultByPlanId(long planId, long day);
//
    List<TrainingActionRecordResultVo> listResultByRecordId(long recordId);
//
//    List<TrainingRecordResult> listResultByActionRecordId(long actionRecordId);
//
//    RecordResultPictureVo getResultPicture(long id, int index);
//
//    RecordResultPictureVo updatePictureKeyPoints(UpdateRecordResultPictureRequest recordResultPictureRequest);
//
//    TrainingRecordResultPlanVo getResultByRecordId(long recordId);
//
//    ActionVo getTrainingAction(long id);
}
