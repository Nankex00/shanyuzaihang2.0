package com.fushuhealth.recovery.device.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
public class PredictWarnResponse {
    private Long id;
    private String monthAge;
    private Date warnStart;
    private Date warnEnd;
    private String warnStatus;
    private String warnResult;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date submitTime;
    private Long childId;
}
