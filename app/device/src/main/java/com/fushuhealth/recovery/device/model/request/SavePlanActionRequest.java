package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

@Data
public class SavePlanActionRequest {

    private Long actionId;

    private Long actionVideoId;

    private Integer cycles;

    private Integer everyDuration;

//    private Boolean check;

//    private Integer sort;

    private Integer restTime;
}
