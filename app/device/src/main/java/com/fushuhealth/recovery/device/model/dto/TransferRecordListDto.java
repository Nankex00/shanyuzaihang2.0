package com.fushuhealth.recovery.device.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/10/4
 */
@Data
public class TransferRecordListDto {
    private Long id;
    private String name;
    private String sex;
    private String age;
    private Byte dangerLevel;
    private String operation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date operationTime;
    private Long transferInstitution;
    private Long receiveInstitution;
    private String reason;
    private String reply;
}
