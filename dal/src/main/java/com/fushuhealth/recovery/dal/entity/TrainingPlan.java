package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TrainingPlan {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String planName;

    private Long planTemplateId;

    private Long coverFileId;

    private String target;

    private Long userId;

    private Long patientId;

    private Integer actionCount;

    private Long timeConsuming;

    private Long startTime;

    private Long endTime;

    private Integer days;

    private String frequency;

    private Integer progressRate;

    private Byte trainingStatus;

    private Long totalTrainedTime;

    private Integer totalPlanDay;

    private Integer totalTrainedDay;

    private Integer totalPlanCount;

    private Integer totalTrainedCount;

    private Integer num;

    private Byte unit;

    private String batch;

    private Byte status;

    private Long created;

    private Long updated;
}