package com.fushuhealth.recovery.dal.dto;

import lombok.Data;

@Data
public class ScaleTableResult {

    private Integer minScore;

    private Integer maxScore;

    private String result;

    private String remark;
}
