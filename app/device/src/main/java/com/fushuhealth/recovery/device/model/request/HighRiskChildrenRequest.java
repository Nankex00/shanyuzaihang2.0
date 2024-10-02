package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class HighRiskChildrenRequest {
    private String query;
    private Byte type;
    private List<String> risks;
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;
}
