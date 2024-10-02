package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.device.model.request.PredictWarnRequest;
import com.fushuhealth.recovery.device.model.response.PredictWarnListResponse;
import com.fushuhealth.recovery.device.model.response.PredictWarnResponse;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
public interface IPredictWarnService {
    BaseResponse<List<PredictWarnResponse>> searchPredictByChildId(Long id);

    BaseResponse<List<PredictWarnListResponse>> searchList(PredictWarnRequest request);
}
