package com.fushuhealth.recovery.dal.vo.h5;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fushuhealth.recovery.dal.vo.h5.ScaleTableQuestionVo;
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
