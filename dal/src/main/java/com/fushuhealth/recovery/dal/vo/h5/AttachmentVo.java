package com.fushuhealth.recovery.dal.vo.h5;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class AttachmentVo {

    private String url;

    private Byte type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String coverUrl;
}
