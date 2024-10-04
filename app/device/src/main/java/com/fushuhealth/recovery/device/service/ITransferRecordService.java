package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.dal.entity.TransferRecord;
import com.fushuhealth.recovery.device.model.request.TransferRecordListRequest;
import com.fushuhealth.recovery.device.model.request.TransferRequest;
import com.fushuhealth.recovery.device.model.response.EvaluateRecordListResponse;
import com.fushuhealth.recovery.device.model.response.TransferRecordListResponse;
import com.fushuhealth.recovery.device.model.response.TransferRecordResponse;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
public interface ITransferRecordService {
    int transferInstitution(TransferRequest request);

    BaseResponse<List<TransferRecordResponse>> searchTransferRecord(Long childId);

    BaseResponse<List<TransferRecordListResponse>> searchDeptList(TransferRecordListRequest request);
}
