package com.fushuhealth.recovery.device.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    private List<ScaleTableQuestionAnswerVo> answers;

}
