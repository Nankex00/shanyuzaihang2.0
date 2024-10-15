package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

@Data
public class DDSTListVo {

    private Byte code;

    private String name;

    private Byte status;

    //建议开始时间
    private String suggestStartTime;

    //建议结束时间
    private String suggestEndTime;

    private Long recordId;

    private Long childrenId;
}
