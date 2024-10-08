package com.fushuhealth.recovery.device.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/10/8
 */
@Data
public class PermissionRequest {
    @NotNull(message = "机构id不能为空")
    private Long deptId;
    @NotNull(message = "操作权限不能为空")
    private List<Long> menuList;
    @NotNull(message = "数据权限不能为空")
    private String dataScope;
}
