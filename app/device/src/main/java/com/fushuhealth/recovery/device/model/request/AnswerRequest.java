package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

import java.util.List;

@Data
public class AnswerRequest {

    private Integer questionSn;

    private List<Integer> answerSn;

    private String remark;

    private List<AttachmentRequest> attachments;
}
