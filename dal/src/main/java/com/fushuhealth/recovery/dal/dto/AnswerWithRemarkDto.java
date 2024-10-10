package com.fushuhealth.recovery.dal.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnswerWithRemarkDto {

    private String remark;

    private String optionSn;

    private Integer questionSn;

    private Integer doctorScore;

    private List<AttachmentDto> attachmentDtos;
}
