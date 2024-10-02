package com.fushuhealth.recovery.device.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictParamReportVo {
    private String paramUrl;
    private String aiAnswer;
    private String remark;
    private String predictWarnQuestion;
}
