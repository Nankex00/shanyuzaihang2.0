package com.fushuhealth.recovery.device.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class TrainingPlanTemplateListVo {

    private Long id;

    private String name;

    private Integer actionCount;

    private String created;

    private String coverUrl;

    private List<TrainingPlanTemplateActionVo> actionList;
}
