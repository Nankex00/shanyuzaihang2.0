package com.fushuhealth.recovery.dal.dto;

import lombok.Data;

@Data
public class Option {

    private Integer sn;

    private String content;

    private Integer score;

    private OptionNext next;
}
