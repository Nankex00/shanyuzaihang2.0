package com.fushuhealth.recovery.common.constant;

public enum FunType {

    MENU((byte) 1, "页面"),
    BUTTON((byte) 2, "按钮"),
    DATA((byte) 3, "数据");

    private final Byte type;
    private final String desc;

    FunType(Byte type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static FunType getStatus(Byte status) {

        for (FunType baseStatus : FunType.values()) {
            if (status.equals(baseStatus.getType())) {
                return baseStatus;
            }
        }
        throw new IllegalArgumentException();
    }

    public Byte getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
