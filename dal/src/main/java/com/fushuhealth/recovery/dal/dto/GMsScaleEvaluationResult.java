package com.fushuhealth.recovery.dal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class GMsScaleEvaluationResult extends BaseScaleEvaluationResult{

    private String stage;

    private String stageResult;

    private Long nextReserve;

    private String suggest;

    @JsonIgnore
    private String gmsCopyrighting;

    private String remark;

    private List<GMsResultIterm> result;
}
