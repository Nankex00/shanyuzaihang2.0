package com.fushuhealth.recovery.device.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@Data
public class TransferRequest {
    @NotNull(message = "id不能为空")
    private Long childId;
    @NotNull(message = "转诊机构id不能为空")
    private Long transferId;
    @NotNull(message = "接诊机构id不能为空")
    private Long receiveId;
    @NotNull(message = "转诊原因不能为空")
    private String reason;
}
