package com.fushuhealth.recovery.device.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class EvaluateRecordResponse {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date submitTime;
    private String operateDept;
    private String monthAge;
}
