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
public class ChildrenResponse {
    private Long id;
    private String name;
    private String sex;
    private String dateOfBrith;
    private Integer age;
    private String dangerLevel;
    private String socialName;
    private String deptName;
}
