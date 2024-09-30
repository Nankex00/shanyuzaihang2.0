package com.fushuhealth.recovery.dal.entity;

import com.fushuhealth.recovery.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRecord extends BaseEntity {
    private Long id;
    private Date operatedTime;
    private Long transferInstitution;
    private Long receiveInstitution;
    private String operation;
    private String reason;
    private Long childId;
}
