package com.fushuhealth.recovery.dal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fushuhealth.recovery.dal.dto.BaseScaleEvaluationResult;
import lombok.Data;

@Data
public class GMs8Result extends BaseScaleEvaluationResult {

    private String stage;

    private String stageResult;

    private Long nextReserve;

    private String suggest;

    private String remark;
}
