package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.util.List;

@Data
public class DDSTQuestionVo extends ScaleTableQuestionVo {

    private Byte subjectCode;

    private String subject;

    //状态，1：未上传， 2：已上传，
    private Byte status;

    private String remark;

    private List<AttachmentVo> attachments;

    private List<String> videos;

    private List<String> pictures;
}
