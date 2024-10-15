package com.fushuhealth.recovery.common.constant;

public enum DataStatus {

    UPLOAD_ERROR((byte)-2, "上传失败"),

    ERROR((byte)-1, "错误"),

    All((byte)0, "全部"),

    WAIT_UPLOAD((byte)6, "待上传"),

    UPLOADED((byte)8, "已上传");

    private final Byte status;
    private final String desc;

    DataStatus(Byte status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static DataStatus getStatus(Byte status) {

        for (DataStatus dataStatus : DataStatus.values()) {
            if (status.equals(dataStatus.getStatus())) {
                return dataStatus;
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
