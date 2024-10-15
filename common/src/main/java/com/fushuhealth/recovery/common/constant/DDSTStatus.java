package com.fushuhealth.recovery.common.constant;

public enum DDSTStatus {

    WAIT_START((byte)2, "待开始"),
    IN_PROGRESS((byte)1, "进行中"),
    FINISHED((byte)3, "已完成"),
    EXPIRED((byte)4, "已过时间"),
    UNKNOWN((byte)0, "未知")
    ;

    private Byte code;

    private String desc;

    DDSTStatus(Byte code, String desc) {
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
