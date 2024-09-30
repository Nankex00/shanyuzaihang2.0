package com.fushuhealth.recovery.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@AllArgsConstructor
@Getter
public enum WarnResultType {
    UNKNOWN((byte)1,"无法评估"),
    PASS((byte)2,"预警通过"),
    FAIL((byte)3,"预警不通过"),
    ASSESSING((byte)4,"评估中");

    private Byte type;
    private String warnResult;
    public static String findWarnResultByType(byte type) {
        for (WarnResultType enumValue : WarnResultType.values()) {
            if (enumValue.getType() == type) {
                return enumValue.getWarnResult();
            }
        }
        return null; // 如果找不到匹配的type，则返回null或者其他适当的值
    }
}
