package com.fushuhealth.recovery.dal.vo.h5;

import lombok.Data;

import java.util.List;

@Data
public class ScaleRecordQuestionVo {

    private Integer id;

    private String type;

    private String name;

    private List<String> answer;

    private Integer userScore;

    private Integer doctorScore;

//    private List<AttachmentVo> attachments;
}
