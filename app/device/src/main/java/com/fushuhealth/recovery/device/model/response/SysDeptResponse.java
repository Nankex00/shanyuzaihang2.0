package com.fushuhealth.recovery.device.model.response;

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
public class SysDeptResponse {

    private Long userId;
    private Long deptId;

    private String deptName;

    private Long institutionLevel;

    private Long parentId;

    private String parentDeptName;

    private String ancestors;

    private String contactNumber;

    private String doctor;

    private String address;
}
