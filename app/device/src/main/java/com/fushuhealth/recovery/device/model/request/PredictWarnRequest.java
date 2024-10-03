package com.fushuhealth.recovery.device.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

//import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class PredictWarnRequest {
    private String query;
    private Byte type;
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;
}
