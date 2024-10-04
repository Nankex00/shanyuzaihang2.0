package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordEditRequest;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordListRequest;
import com.fushuhealth.recovery.device.model.request.EvaluateRecordRequest;
import com.fushuhealth.recovery.device.model.response.EvaluateRecordDetail;
import com.fushuhealth.recovery.device.model.response.EvaluateRecordListResponse;
import com.fushuhealth.recovery.device.model.response.EvaluateRecordResponse;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
public interface IEvaluateRecordService {
    int add(EvaluateRecordRequest request);

    BaseResponse<List<EvaluateRecordResponse>> searchListByChildId(Long childId);

    EvaluateRecordDetail searchDetail(Long id);

    int editDetail(EvaluateRecordEditRequest request);

    int delete(Long id);

    BaseResponse<List<EvaluateRecordListResponse>> searchDeptList(EvaluateRecordListRequest request);
}
