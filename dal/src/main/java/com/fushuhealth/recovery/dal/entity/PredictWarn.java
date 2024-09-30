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
public class PredictWarn {
    private Long id;
    private Byte monthAge;
    private Date warnStart;
    private Date warnEnd;
    private Byte warnStatus;
    private Byte warnResult;
    private Date submitTime;
    private Long childId;
}
