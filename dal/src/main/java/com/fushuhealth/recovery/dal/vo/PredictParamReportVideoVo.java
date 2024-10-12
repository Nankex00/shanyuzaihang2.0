package com.fushuhealth.recovery.dal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zhuanz
 * @date 2024/10/11
 */
@Data
@AllArgsConstructor
public class PredictParamReportVideoVo {
    private String paramUrl;
    private Long scaleEvaluationId;
}
