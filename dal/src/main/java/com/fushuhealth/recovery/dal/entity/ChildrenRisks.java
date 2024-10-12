package com.fushuhealth.recovery.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhuanz
 * @date 2024/10/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenRisks {
    private Long childId;
    private Long riskId;
    private Byte type;
}
