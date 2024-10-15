package com.fushuhealth.recovery.common.constant;

public enum WorkScheduleType {

    ALL((byte)0, " 全部"),
    CLINIC_EVALUATE((byte)1, "门诊评估"),
    RECOVERY_GUIDE_OFFLINE((byte)2, "康复指导线下"),
    AI_EVALUATE((byte)3, "智能评估"),
    VIDEO_GUIDE((byte)4, "视频指导"),
    CLINIC_EVALUATE_AND_RECOVERY_GUIDE((byte)5, "门诊评估+康复指导"),

    RECOVERY_GUIDE_ONLINE((byte)6, "康复指导线上"),
    LEARNING_DISABILITY((byte)7, "学习障碍");

    private byte type;

    private String desc;

    WorkScheduleType(byte type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static WorkScheduleType getType(byte type) {
        for (WorkScheduleType workScheduleType : WorkScheduleType.values()) {
            if (workScheduleType.getType() == (type)) {
                return workScheduleType;
            }
        }
        return ALL;
    }

    public byte getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
