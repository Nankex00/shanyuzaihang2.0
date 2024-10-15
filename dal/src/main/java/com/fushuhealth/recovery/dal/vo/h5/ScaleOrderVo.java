package com.fushuhealth.recovery.dal.vo.h5;

import com.fushuhealth.recovery.dal.vo.h5.ScaleOrderListVo;
import lombok.Data;

@Data
public class ScaleOrderVo extends ScaleOrderListVo {

    private String paidFee;

    private String orderNo;

    private String paidTime;

    private String usedTime;

    private String page;
}
