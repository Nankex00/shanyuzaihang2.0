package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ScaleOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long userId;

    private Long doctorId;

    private String title;

    //预约 id
    private String resourceId;

    private String productId;

    private String xiaoeOrderId;

    private String scaleEvaluationRecordId;

    private Byte scaleTableCode;

    private Long childrenId;

    private Integer totalFee;

    private Integer paidFee;

    private Integer refundable;

    private Integer refunded;

    private Long payTime;

    private String payChannel;

    private String payNo;

    private Integer totalTimes;

    private Integer availableTimes;

    private Integer usedTimes;

    private Byte orderStatus;

    private Byte orderType;

    private Byte payment;

    private String invoiceFileId;

    private String reason;

    private Long organizationId;

    private String appId;

    private String channel;

    private String orgId;

    //1:线上评测，2：线下评测
    private Byte useWay;

    private Byte status;

    private Long created;

    private Long updated;
}
