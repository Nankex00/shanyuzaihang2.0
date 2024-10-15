package com.fushuhealth.recovery.dal.vo.h5;

import lombok.Data;

@Data
public class ScaleOrderListVo {

    private Long id;

    private String name;

    private String statusString;

    private Byte status;

    private String created;

    private String totalFee;

    private Byte scaleTableCode;

    private Byte orderType;
}
