package com.fushuhealth.recovery.web.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ScaleVideoMarkListVo {

    private Long id;

    private String tag;

    private String startTime;

    private String endTime;

    @JsonIgnore
    private Byte source;
}
