package com.fushuhealth.recovery.device.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChildrenDetail extends ChildrenResponse{
    private Long deptId;
    private Integer gestationalWeeks;
    private Integer gestationalWeekDay;
    private Integer brithWight;
    private String dangerOfChild;
    private String dangerOfMother;
    private String diagnose;
    private String telephone;
    private String identification;
}
