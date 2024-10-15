package com.fushuhealth.recovery.common.constant;

public enum DataUploadSource {

    UNKNOWN((byte) 0, "未知"),
    MINI_APP((byte) 1, "小程序"),
    MECHANISM((byte) 2, "机构端"),
    WEB((byte) 3, "网页"),;

    public static DataUploadSource getByCode(Byte code) {
        for (DataUploadSource dataUploadSource : DataUploadSource.values()) {
            if (dataUploadSource.getCode().equals(code)) {
                return dataUploadSource;
            }
        }
        return UNKNOWN;
    }

    private final Byte code;

    private final String desc;

    DataUploadSource(Byte code, String desc) {
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
