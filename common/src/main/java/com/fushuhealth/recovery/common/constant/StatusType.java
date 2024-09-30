package com.fushuhealth.recovery.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@AllArgsConstructor
@Getter
public enum StatusType {
    TIMEOUT((byte)1,"已过时间"),
    FINISH((byte)2,"已完成"),
    UNDERWAY((byte)3,"进行中"),
    WAITING((byte)4,"未开始");
    private final Byte type;
    private final String status;

    public static String findStatusByType(byte type) {
        for (StatusType enumValue : StatusType.values()) {
            if (enumValue.getType() == type) {
                return enumValue.getStatus();
            }
        }
        return null; // 如果找不到匹配的type，则返回null或者其他适当的值
    }
}
