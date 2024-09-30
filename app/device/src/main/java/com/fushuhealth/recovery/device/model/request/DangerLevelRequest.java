package com.fushuhealth.recovery.device.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DangerLevelRequest {
    @NotNull(message = "id不能为空")
    private Long childId;
    @NotNull(message = "危险等级不能为空")
    private Byte dangerLevel;
    @NotNull(message = "变更原因不能为空")
    private String reason;
}
