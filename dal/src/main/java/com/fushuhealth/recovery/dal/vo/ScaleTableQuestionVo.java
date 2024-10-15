package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fushuhealth.recovery.dal.vo.ScaleTableQuestionAnswerVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScaleTableQuestionVo {

    private Integer sn;

    private String name;

    private Byte type;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> carousels;

    private List<String> attachmentType;

    private List<ScaleTableQuestionAnswerVo> answers;

    private String introduction;

}
