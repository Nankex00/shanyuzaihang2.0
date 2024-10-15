package com.fushuhealth.recovery.h5.model.request;

import com.fushuhealth.recovery.h5.model.request.AttachmentRequest;
import lombok.Data;

import java.util.List;

@Data
public class AnswerRequest {

    private Integer questionSn;

    private List<Integer> answerSn;

    private String remark;

    private List<AttachmentRequest> attachments;
}
