package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.util.List;

@Data
public class TrainingPlanTemplateActionVo {

    private Long actionId;

    private String actionName;

    private Long actionVideoId;

    private String coverUrl;

    private String videoUrl;

    private Integer cycles;

    private Integer everyDuration;

//    private Boolean check;

//    private Integer sort;

    private Integer restTime;

    private List<PositionFileVo> videos;
}
