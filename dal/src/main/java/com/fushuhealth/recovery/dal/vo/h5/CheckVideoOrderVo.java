package com.fushuhealth.recovery.dal.vo.h5;

import lombok.Data;

@Data
public class CheckVideoOrderVo {

    private Boolean hasPaidOrder;

    private Long orderId;

    private String appId;

    private String page;

    private String resourceId;
}
