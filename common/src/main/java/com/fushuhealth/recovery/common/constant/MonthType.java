package com.fushuhealth.recovery.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Getter
@AllArgsConstructor
public enum MonthType {
    THREE((byte)1,"3月龄"),
    SIX((byte)2,"6月龄"),
    EIGHT((byte)3,"8月龄"),
    TWELVE((byte)4,"12月龄"),
    EIGHTEEN((byte)5,"18月龄"),
    TWENTY_FOUR((byte)6,"24月龄"),
    THIRTY((byte)7,"30月龄"),
    THIRTY_SIX((byte)8,"36月龄");

    private Byte type;
    private String MonthAge;

    public static String findMonthByType(byte type) {
        for (MonthType enumValue : MonthType.values()) {
            if (enumValue.getType() == type) {
                return enumValue.getMonthAge();
            }
        }
        return null; // 如果找不到匹配的type，则返回null或者其他适当的值
    }

    public static List<EnumInfo> generateMonthList() {
        List<EnumInfo> monthList = new ArrayList<>();

        for (MonthType enumValue : MonthType.values()) {
            monthList.add(new EnumInfo(enumValue.getType(), enumValue.getMonthAge()));
        }
        return monthList;
    }
}
