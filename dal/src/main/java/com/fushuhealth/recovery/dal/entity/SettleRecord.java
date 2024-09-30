package com.fushuhealth.recovery.dal.entity;

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
public class SettleRecord {
    private Long id;
    private Long operatedId;
    private Byte dangerLevel;
    private String reason;
    private Long childId;
    private Date operatedTime;
}
