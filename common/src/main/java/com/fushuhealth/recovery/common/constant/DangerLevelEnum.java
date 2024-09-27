package com.fushuhealth.recovery.common.constant;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
public enum DangerLevelEnum {
    ONE("Ⅰ类"),
    TWO("Ⅱ类"),
    THREE("Ⅲ类"),
    NORMAL("正常"),
    UNKNOWN("未知");

    private final String dangerLevel;


    DangerLevelEnum(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getDangerLevel(){
        return dangerLevel;
    }
}
