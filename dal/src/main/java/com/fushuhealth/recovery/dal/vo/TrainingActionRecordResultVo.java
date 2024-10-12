package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

@Data
public class TrainingActionRecordResultVo {

    private Long id;

    private String time;

    private Long actionId;

    private String actionName;

    private Integer planCount;

    private Integer finishCount;

    private Integer effectCount;

    private String resultPage;
}
