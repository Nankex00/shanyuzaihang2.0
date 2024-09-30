package com.fushuhealth.recovery.common.constant;

public enum RiskType {

    CHILD_RISK((byte)1, "儿童风险"),
    MOTHER_RISK((byte)2, "母亲风险");

    private Byte type;
    private String name;

    RiskType(Byte type, String name) {
        this.type = type;
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
