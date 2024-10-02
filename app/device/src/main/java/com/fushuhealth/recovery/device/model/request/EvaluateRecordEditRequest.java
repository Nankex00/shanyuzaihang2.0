package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class EvaluateRecordEditRequest {
    @NotNull
    private Long id;
    @NotNull(message = "儿童id不能为空")
    private Long childId;
    @NotNull(message = "月龄不能为空")
    private Byte monthAge;
    @NotNull(message = "geSell结果不能为空")
    private String geSellResult;
    private String geSellUrls;
    @NotNull(message = "s-s结果不能为空")
    private String sSResult;
    private String sSUrls;
    private String otherResult;
    private String otherUrls;
}
