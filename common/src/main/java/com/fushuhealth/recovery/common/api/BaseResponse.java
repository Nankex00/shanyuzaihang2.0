package com.fushuhealth.recovery.common.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zhuanz
 * @date 2024/9/25
 */
@Data
@AllArgsConstructor
@ApiModel(value = "标准出参")
public class BaseResponse<T> {
    @ApiModelProperty(value = "items")
    private T items;
    @ApiModelProperty(value = "数据总数")
    private Long total;
}
