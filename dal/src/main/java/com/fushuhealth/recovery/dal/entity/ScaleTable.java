package com.fushuhealth.recovery.dal.entity;

import com.fushuhealth.recovery.dal.dto.QuestionDto;
import com.fushuhealth.recovery.dal.dto.ScaleTableResult;
import lombok.Data;

import java.util.List;

@Data
public class ScaleTable {

    private String name;

    private Byte code;

    private Byte type;

    private Byte classification;

    private List<QuestionDto> questions;

    private List<String> carouselFileIds;

    private List<ScaleTableResult> results;
}
