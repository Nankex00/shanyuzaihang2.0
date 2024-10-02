package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.device.model.response.PredictParamReportResponse;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
public interface IPredictParamReportService {
    BaseResponse<PredictParamReportResponse> searchDetail(Long predictWarnId);
}
