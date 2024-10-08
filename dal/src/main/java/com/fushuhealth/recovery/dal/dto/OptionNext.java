package com.fushuhealth.recovery.dal.dto;

import lombok.Data;

import java.util.List;

@Data
public class OptionNext {

    //类型，1 单选，2 填空，3 多选
    private Integer type;

    private List<Option> options;
}
