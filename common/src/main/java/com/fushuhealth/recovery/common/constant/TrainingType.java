package com.fushuhealth.recovery.common.constant;

public enum TrainingType {

    UNKNOWN((byte)0, "未知"),
    PLAN((byte)1, "居家端"),
    ACTION((byte)2, "机构端");

    public static TrainingType getType(byte type) {
        for (TrainingType trainingType : TrainingType.values()) {
            if (trainingType.getType().equals(type)) {
                return trainingType;
            }
        }
        return UNKNOWN;
    }

    private Byte type;

    private String desc;

    TrainingType(Byte type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Byte getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
