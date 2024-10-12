package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.device.model.vo.PageVo;
import com.fushuhealth.recovery.device.model.vo.TrainingPlanTemplateListVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListTrainingPlanTemplateResponse {

    private PageVo page;

    private List<TrainingPlanTemplateListVo> templates;
}
