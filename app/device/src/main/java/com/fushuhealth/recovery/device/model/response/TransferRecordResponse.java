package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.dal.entity.TransferRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransferRecordResponse extends TransferRecord {
    private String transferInstitutionName;
    private String receiveInstitutionName;
}
