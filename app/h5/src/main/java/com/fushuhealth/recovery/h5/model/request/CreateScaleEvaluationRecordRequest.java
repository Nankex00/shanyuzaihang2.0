package com.fushuhealth.recovery.h5.model.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateScaleEvaluationRecordRequest {

    private Long orderId;

    private Byte scaleTableCode;

    private Long childrenId;

    private String name;

    private Byte gender;

    private Long birthday;

    private Byte category;

    private List<AnswerRequest> answers;
}
