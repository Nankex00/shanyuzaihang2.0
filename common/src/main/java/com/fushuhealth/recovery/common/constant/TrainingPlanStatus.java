package com.fushuhealth.recovery.common.constant;

public enum TrainingPlanStatus {

    WAITING((byte)1, "未开始"),
    STUDYING((byte)2, "学习中"),
    WAITING_STUDY((byte)3, "未学习"),
    FINISHED((byte)4, "已完成"),
    ERROR((byte)-1, "异常");

    public static TrainingPlanStatus getStatus(byte status) {
        for (TrainingPlanStatus trainingPlanStatus : TrainingPlanStatus.values()) {
            if (trainingPlanStatus.getStatus().equals(status)) {
                return trainingPlanStatus;
            }
        }
        return ERROR;
    }

    private Byte status;

    private String desc;

    TrainingPlanStatus(Byte status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Byte getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
