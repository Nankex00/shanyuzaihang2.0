package com.fushuhealth.recovery.device.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/10/4
 */
@Data
public class EvaluateRecordListDto {
    private Long id;
    private String name;
    private String sex;
    private String dateOfBirth;
    private Byte dangerLevel;
    private Byte monthAge;
    private Date submitTime;
    private String deptName;
}
