package com.fushuhealth.recovery.device.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenRequest {
    private String query;
    private Byte type;
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;
}
