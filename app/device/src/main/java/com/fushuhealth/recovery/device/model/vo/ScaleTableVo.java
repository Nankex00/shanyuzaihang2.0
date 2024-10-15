package com.fushuhealth.recovery.device.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fushuhealth.recovery.dal.vo.ScaleTableQuestionSubjectVo;
import lombok.Data;

import java.util.List;

@Data
public class ScaleTableVo {

    private String name;

    private Byte code;

    @JsonIgnore
    private String classification;

    @JsonIgnore
    private Byte type;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> carousels;

    private List<ScaleTableQuestionSubjectVo> subjects;

}
