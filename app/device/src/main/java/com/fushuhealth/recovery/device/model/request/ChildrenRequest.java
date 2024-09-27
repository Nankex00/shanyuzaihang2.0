package com.fushuhealth.recovery.device.model.request;

import com.fushuhealth.recovery.common.constant.DangerLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenRequest {
    private String query;
    private DangerLevelEnum dangerLevel;
    private Integer pageSize;
    private Integer pageNum;
}
