package com.fushuhealth.recovery.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhuanz
 * @date 2024/9/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private T items;
    private Long total;
}
