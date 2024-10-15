package com.fushuhealth.recovery.common.constant;

public enum OrderType {

    UNKNOWN((byte)0, "未知"),
    SCALE_TABLE_ORDER((byte)1, "量表订单"),
    VIDEO_ORDER((byte)2, "视频订单"),
    VIDEO_GUIDE((byte)3, "视频一对一指导订单"),

    CLINIC_EVALUATE((byte)4, "门诊评估订单"),
    RECOVERY_GUIDE((byte)5, "康复指导订单");

    private Byte code;

    private String desc;

    OrderType(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderType getOrderType(byte code) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.getCode().equals(code)) {
                return orderType;
            }
        }
        return UNKNOWN;
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
