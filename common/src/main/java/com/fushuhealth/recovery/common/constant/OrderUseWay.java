package com.fushuhealth.recovery.common.constant;

public enum OrderUseWay {
    ONLINE((byte)1, "线上"),
    OFFLINE((byte)2, "线下");

    public static OrderUseWay getOrderUseWay(byte code) {
        for (OrderUseWay orderUseWay : OrderUseWay.values()) {
            if (orderUseWay.getCode() == code) {
                return orderUseWay;
            }
        }
        return ONLINE;
    }
    private Byte code;
    private String desc;

    OrderUseWay(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
