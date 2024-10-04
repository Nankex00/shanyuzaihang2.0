package com.fushuhealth.recovery.device.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/10/4
 */
@Data
public class TransferRecordListResponse {
    private Long id;
    private String name;
    private String sex;
    private String age;
    private String dangerLevel;
    private String operation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String operationTime;
    private String transferInstitutionName;
    private String receiveInstitutionName;
    private String reason;
    private String reply;
}
