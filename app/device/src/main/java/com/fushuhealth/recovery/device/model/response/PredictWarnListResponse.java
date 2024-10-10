package com.fushuhealth.recovery.device.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class PredictWarnListResponse {
    private Long id;
    private String name;
    private String sex;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Long dateOfBirth;
    private String dangerLevel;
    private String monthAge;
    private String warnResult;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date submitTime;
}
