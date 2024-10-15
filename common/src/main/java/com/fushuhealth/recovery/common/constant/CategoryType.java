package com.fushuhealth.recovery.common.constant;

public enum CategoryType {

    ALL((byte)0, " 全部"),
    NORMAL((byte)1, "普通"),
    MITOCHONDRIAL_DISEASE((byte)2, "线粒体病"),
    DEVELOPMENTAL_RISK_MANAGEMENT_REHABILITATION_0_1((byte)3, "0-1岁发育风险管理康复"),

    SCAN_CODE_REGISTER((byte)4, "扫码登记" ),
    LEARNING_DISABILITY((byte)5, "学习障碍");

    private byte type;

    private String desc;

    CategoryType(byte type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static CategoryType getType(byte type) {
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryType.getType() == (type)) {
                return categoryType;
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
