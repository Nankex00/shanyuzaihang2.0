package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.dal.entity.TrainingPlanTemplate;
import com.fushuhealth.recovery.dal.vo.TrainingPlanTemplateVo;
import com.fushuhealth.recovery.device.model.request.SavePlanTemplateRequest;
import com.fushuhealth.recovery.device.model.request.UpdateTrainingPlanTemplateRequest;
import com.fushuhealth.recovery.device.model.response.ListTrainingPlanTemplateResponse;

public interface TrainingPlanTemplateService {

    ListTrainingPlanTemplateResponse listTemplate(int pageNo, int pageSize, long userId, String name);

    void saveTrainingPlanTemplate(long userId, SavePlanTemplateRequest request);

    void deleteTrainingPlanTemplate(long id);
//
    TrainingPlanTemplateVo getTrainingPlanTemplateVo(long id);

    void updateTrainingPlanTemplate(UpdateTrainingPlanTemplateRequest request);

    TrainingPlanTemplate getTrainingPlanTemplate(long id);
}
