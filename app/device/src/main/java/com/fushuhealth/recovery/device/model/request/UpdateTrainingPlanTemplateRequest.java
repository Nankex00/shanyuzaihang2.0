package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTrainingPlanTemplateRequest {

    private Long id;

    private String name;

    private List<SavePlanActionRequest> actions;
}
