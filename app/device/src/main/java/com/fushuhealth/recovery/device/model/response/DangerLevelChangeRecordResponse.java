package com.fushuhealth.recovery.device.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DangerLevelChangeRecordResponse {
    private Long id;
    private Long operatedInstitution;
    private String operatedInstitutionName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date operatedTime;
    private Byte dangerLevel;
    private String reason;
    private Long childId;
}
