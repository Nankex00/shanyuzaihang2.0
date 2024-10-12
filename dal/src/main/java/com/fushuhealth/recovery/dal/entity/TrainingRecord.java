package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TrainingRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long planId;

    private Long trainingDay;

    private Long trainingTime;

    private Byte trainingType;

    private Long duration;

    private Integer totalAction;

    private Integer trainedAction;

    private Integer trainingProgress;

    private Byte reportStatus;

    private Long userId;

    private String remark;

    private String doctorName;

    private Byte status;

    private Long created;

    private Long updated;
}
