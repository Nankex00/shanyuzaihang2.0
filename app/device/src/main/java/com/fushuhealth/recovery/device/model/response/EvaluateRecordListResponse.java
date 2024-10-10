package com.fushuhealth.recovery.device.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/10/4
 */
@Data
public class EvaluateRecordListResponse {
    private Long id;
    private String name;
    private String sex;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Long dateOfBirth;
    private String dangerLevel;
    private String monthAge;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date submitTime;
    private String deptName;
}
