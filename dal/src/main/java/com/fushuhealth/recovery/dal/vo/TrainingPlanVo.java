package com.fushuhealth.recovery.dal.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class TrainingPlanVo {

    private Long id;

    private String planName;

    private String target;

    private String startTime;

    private String endTime;

    private String trainingStatus;

    private Integer planActionCount;

    private String totalTrainedTime;

    private Integer totalPlanDay;

    private Integer totalTrainedDay;

    private Integer totalPlanCount;

    private Integer totalTrainedCount;

    private Integer num;

    private Byte unit;

    private List<Integer> frequency;

    private List<PlanActionListVo> actions;

    private Long patientId;

    private String patientName;

    @JsonIgnore
    private Long templateId;
}
