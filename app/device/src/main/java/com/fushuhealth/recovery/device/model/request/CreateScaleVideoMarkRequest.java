package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

@Data
public class CreateScaleVideoMarkRequest {

    private Long id;

    private Long recordId;

    private Integer questionId;

    private Long fileId;

    private String startTime;

    private String endTime;

    private String tag;
}
