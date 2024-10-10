package com.fushuhealth.recovery.common.constant;

public enum Gender {

    MALE((byte)1, "男"),
    FEMALE((byte)2, "女"),
    UNKNOWN((byte)0, "未知"),
    ALL((byte)3, "所有");

    private final Byte code;

    private final String desc;

    public static Gender getGender(byte code) {
        for (Gender gender : Gender.values()) {
            if (gender.getCode() == code) {
                return gender;
            }
        }
        return UNKNOWN;
    }

    public static Gender getGender(String desc) {
        for (Gender gender : Gender.values()) {
            if (gender.getDesc().equals(desc)) {
                return gender;
            }
        }
        return UNKNOWN;
    }

    Gender(Byte code, String desc) {
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
