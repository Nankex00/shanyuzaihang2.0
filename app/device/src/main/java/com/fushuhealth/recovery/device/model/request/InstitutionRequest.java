package com.fushuhealth.recovery.device.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionRequest {
    private String InstitutionName;

    private String type;

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;

    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;

}
