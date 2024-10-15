package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.util.List;

@Data
public class ActionVideoVo {

    private Long id;

    private String actionName;

    private String duration;

    private String analysisPosition;

    private String fileName;

    List<PositionFileVo> videos;
}
