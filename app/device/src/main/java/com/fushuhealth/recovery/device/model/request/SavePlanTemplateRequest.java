package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

import java.util.List;

@Data
public class SavePlanTemplateRequest {

    private String name;

    private List<SavePlanActionRequest> actions;
}
