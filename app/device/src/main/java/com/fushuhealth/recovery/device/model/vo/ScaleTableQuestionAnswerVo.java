package com.fushuhealth.recovery.device.model.vo;

import lombok.Data;

@Data
public class ScaleTableQuestionAnswerVo {

    private Integer sn;

    private Boolean selected = false;

    private String content;
}
