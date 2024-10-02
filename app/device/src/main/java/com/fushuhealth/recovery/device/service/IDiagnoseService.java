package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.dal.entity.Diagnose;
import com.fushuhealth.recovery.dal.entity.DiagnoseRecord;
import com.fushuhealth.recovery.device.model.request.DiagnoseRequest;
import com.fushuhealth.recovery.device.model.request.SettleRequest;
import com.fushuhealth.recovery.device.model.response.DiagnoseResponse;
import lombok.Data;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */

public interface IDiagnoseService {
    BaseResponse<List<DiagnoseResponse>> list();

    int settleDiagnose(SettleRequest request);

    BaseResponse<List<Long>> searchDiagnoseByChildId(Long childId);

    int addDiagnoseRecord(DiagnoseRequest request);
}
