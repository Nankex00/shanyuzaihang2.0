package com.fushuhealth.recovery.common.constant;

public enum Enable {

    DISABLE((byte) 0, "禁用"),
    ENABLE((byte) 1, "启用");

    private byte code;

    private String desc;

    Enable(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static Enable getEnable(byte code) {
        for (Enable enable : Enable.values()) {
            if (enable.getCode() == code) {
                return enable;
            }
        }
        return DISABLE;
    }

    public static boolean isEnable(byte status) {
        return ENABLE == getEnable(status);
    }
}
