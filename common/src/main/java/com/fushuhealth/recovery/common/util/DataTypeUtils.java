package com.fushuhealth.recovery.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/10/8
 */
public class DataTypeUtils {
    public static List<Long> IntegerToLongTypeHandler(List<Integer> integerList){
        // 创建一个新的 List<Long> 用于存储转换后的数据
        List<Long> longList = new ArrayList<>();

        // 遍历 List<Integer>，将每个 Integer 元素转换为 Long 类型，并添加到新的 List<Long> 中
        for (Integer num : integerList) {
            longList.add(num.longValue());
        }
        return longList;
    }

}
