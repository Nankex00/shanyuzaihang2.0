package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class AttachmentVo {

    private Long fileId;

    private String url;

    private Byte type;

    private String position;

    private String videoType;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String coverUrl;
}
