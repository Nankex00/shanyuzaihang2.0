package com.fushuhealth.recovery.dal.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class QuestionDto {

    private Integer sn;

    private String name;

    private Integer maxMonth;

    private Integer minMonth;

    private List<String> attachmentType = Arrays.asList("VIDEO");

    //科目
    private Byte subject;

    private Byte subcategory = 0;

    //类型，单选，多选，判断
    private Byte type;

    private List<String> carouselFileIds;

    private List<Option> options;

    private String introduction;
}
