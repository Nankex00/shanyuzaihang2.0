package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ScaleTableQuestionSubjectVo {

    @JsonIgnore
    private Byte subjectCode;

    private String subject;

    private Integer sum;

    private List<ScaleTableQuestionVo> questions;
}
