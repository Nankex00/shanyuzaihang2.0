package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.device.model.vo.PredictParamReportVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictParamReportResponse {
    private List<PredictParamReportVo> voList;
    private String monthAge;
    private String warnStatus;
}
