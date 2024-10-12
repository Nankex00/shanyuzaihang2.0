package com.fushuhealth.recovery.dal.entity;

import lombok.Data;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class PredictParamReport {
    private Long id;
    private Long quantificationId;
    private Byte aiAnswer;
    private String remark;
    private Long predictWarnId;
}

