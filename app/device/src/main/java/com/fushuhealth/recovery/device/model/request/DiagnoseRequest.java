package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
public class DiagnoseRequest {
    @NotNull(message = "儿童id不能为空")
    private Long childId;
    @NotNull(message = "机构id不能为空")
    private Long deptId;
    @NotNull(message = "高危等级不能为空")
    private Byte dangerLevel;
    @NotEmpty(message = "诊断结果不能为空")
    private List<Long> diagnoses;
}
