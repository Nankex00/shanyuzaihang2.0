package com.fushuhealth.recovery.device.model.dto;

import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.device.model.response.ChildrenDetail;
import lombok.Data;

/**
 * @author Zhuanz
 * @date 2024/10/4
 */
@Data
public class ChildrenDetailDto extends Children {
    private String deptName;
}
