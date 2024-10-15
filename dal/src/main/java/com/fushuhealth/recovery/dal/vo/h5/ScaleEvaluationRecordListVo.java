package com.fushuhealth.recovery.dal.vo.h5;

import lombok.Data;

@Data
public class ScaleEvaluationRecordListVo {

    private Long id;

    private String scaleClassification;

    private String scaleName;

    private String time;

    private Byte scaleTableCode;

    private Byte type;

    private Byte reserveType;

    private Byte progressStatusByte;

    private String progressStatus;

    private Byte useWay;
}
