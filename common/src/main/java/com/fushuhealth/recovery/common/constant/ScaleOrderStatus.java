package com.fushuhealth.recovery.common.constant;

public enum ScaleOrderStatus {

    ERROR((byte)-1, "错误"),

    ALL((byte)0, "全部" ),
    WAIT_PAY((byte)1, "待支付"),
    PAID((byte)2, "已付款"),
    USED((byte)3, "已使用"),
    CANCELED((byte)4, "已取消"),
    WAIT_REVIEW((byte)5, "待审核"),
    REJECT((byte)6, "已驳回"),
    ;

    public static ScaleOrderStatus getScaleOrderStatus(byte code) {
        for (ScaleOrderStatus status : ScaleOrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return ERROR;
    }

    private Byte code;

    private String desc;

    ScaleOrderStatus(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
