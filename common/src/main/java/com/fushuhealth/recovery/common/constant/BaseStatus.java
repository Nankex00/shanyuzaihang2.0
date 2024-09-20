package com.fushuhealth.recovery.common.constant;

public enum BaseStatus {

    //记录状态。1：正常，0：禁用，-1：删除
    NORMAL((byte) 1, "正常"),
    DELETE((byte) -1, "删除"),
    INVALID((byte) 0, "失效");

    private final Byte status;
    private final String desc;

    BaseStatus(Byte status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static BaseStatus getStatus(Byte status) {

        for (BaseStatus baseStatus : BaseStatus.values()) {
            if (status.equals(baseStatus.getStatus())) {
                return baseStatus;
            }
        }
        throw new IllegalArgumentException();
    }

    public Byte getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

}
