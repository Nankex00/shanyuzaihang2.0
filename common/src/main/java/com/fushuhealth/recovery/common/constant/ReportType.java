package com.fushuhealth.recovery.common.constant;

public enum ReportType {

    UNKNOWN((byte)0, "未知"),
    MANUAL((byte)1, "手工版"),
    SIMPLE((byte)2, "简约版"),
    STANDARD((byte)3, "标准版");

    public static ReportType getReportType(byte code) {
        for (ReportType reportType : ReportType.values()) {
            if (reportType.getCode() == code) {
                return reportType;
            }
        }
        return UNKNOWN;
    }

    private Byte code;

    private String desc;

    ReportType(Byte code, String desc) {
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
