package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

@Data
public class TrainingRecordVo {

    private Long id;

    private String duration;

    private String startTime;

    private String reportStatus;

    private String planName;

    private Long planId;

    private Byte TrainingType;

    private String remark;

    private String doctorName;
}
