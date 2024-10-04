package com.fushuhealth.recovery.device.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Zhuanz
 * @date 2024/10/4
 */
@Data
public class EvaluateRecordListRequest {
    @Schema(name = "query")
    private String query;
    @Schema(name = "type")
    private Byte type;
    @Schema(name = "pageSize")
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
    @Schema(name = "pageNum")
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;
}
