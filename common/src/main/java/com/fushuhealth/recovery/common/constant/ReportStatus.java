package com.fushuhealth.recovery.common.constant;

public enum ReportStatus {

    RECORDING((byte)0, "录制中"),
    RECORDED((byte)1, "录制完成"),
    PROCESSING((byte)2, "分析中"),
    FINISHED((byte)3, "已完成"),
    ERROR((byte)-1, "异常");

    public static ReportStatus getStatus(byte status) {
        for (ReportStatus reportStatus : ReportStatus.values()) {
            if (reportStatus.getStatus().equals(status)) {
                return reportStatus;
            }
        }
        return ERROR;
    }

    private Byte status;

    private String desc;

    ReportStatus(Byte status, String desc) {
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
