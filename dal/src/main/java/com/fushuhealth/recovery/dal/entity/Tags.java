package com.fushuhealth.recovery.dal.entity;

import lombok.Data;

@Data
public class Tags {

    private Long id;

    private String name;

    private Long parentId;

    private Byte level;

    private Byte classify;

    private Byte status;

    private Long created;

    private Long updated;
}