package com.fushuhealth.recovery.dal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class BaseScaleEvaluationResult {

    private Boolean showVideo;

    @JsonIgnore
    private Map<String, Set<String>> abnormalVideoNames;

    @JsonIgnore
    private Map<String, Set<String>> bigMoveVideoNames;
}
