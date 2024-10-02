package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class RepeatFiltrateEditRequest {
    @NotNull(message = "记录id不能为空")
    private Long id;
    @NotNull(message = "儿童id不能为空")
    private Long childId;
    @NotNull(message = "月龄不能为空")
    private Byte monthAge;
    @NotNull(message = "AQS结果不能为空")
    private String aqsResult;
    private String aqsUrls;
    @NotNull(message = "DDST结果不能为空")
    private String ddstResult;
    private String ddstUrls;
    private String otherResult;
    private String otherUrls;
}
