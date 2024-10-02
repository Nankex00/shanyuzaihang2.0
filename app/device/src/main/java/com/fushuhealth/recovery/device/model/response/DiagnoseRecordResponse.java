package com.fushuhealth.recovery.device.model.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class DiagnoseRecordResponse {
    private Long id;
    private Long operatedId;
    private Date operatedTime;
    private String beforeDiagnose;
    private String diagnoseDetail;
    private Long childId;
}
