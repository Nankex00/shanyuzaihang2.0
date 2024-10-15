package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.util.List;

@Data
public class TrainingPlanTemplateVo {

    private Long id;

    private String name;

    private List<TrainingPlanTemplateActionVo> actions;
}
