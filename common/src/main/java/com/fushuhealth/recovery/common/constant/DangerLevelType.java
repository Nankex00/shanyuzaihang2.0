package com.fushuhealth.recovery.common.constant;

import lombok.Getter;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@Getter
public enum DangerLevelType {

    ONE((byte)1,"Ⅰ类"),
    TWO((byte)2,"Ⅱ类"),
    THREE((byte)3,"Ⅲ类"),
    NORMAL((byte)4,"正常"),
    UNKNOWN((byte)5,"未知");

    private final Byte type;
    private final String dangerLevel;


    DangerLevelType(Byte type,String dangerLevel) {
        this.type = type;
        this.dangerLevel = dangerLevel;
    }

    public static String findDangerLevelByType(byte type) {
        for (DangerLevelType enumValue : DangerLevelType.values()) {
            if (enumValue.getType() == type) {
                return enumValue.getDangerLevel();
            }
        }
        return null; // 如果找不到匹配的type，则返回null或者其他适当的值
    }


}
