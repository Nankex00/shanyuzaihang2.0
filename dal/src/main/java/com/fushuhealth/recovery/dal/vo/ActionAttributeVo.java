package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ActionAttributeVo {

    private String name;

    private String code;

    @JsonIgnore
    private String motionId;

    @JsonIgnore
    private String coordinate;

    @JsonIgnore
    private String position;
}
