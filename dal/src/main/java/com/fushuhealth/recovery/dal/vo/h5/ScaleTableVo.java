package com.fushuhealth.recovery.dal.vo.h5;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
