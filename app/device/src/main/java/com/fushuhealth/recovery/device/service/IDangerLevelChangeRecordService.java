package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.dal.entity.DangerLevelChangeRecord;
import com.fushuhealth.recovery.device.model.request.DangerLevelRequest;
import com.fushuhealth.recovery.device.model.response.DangerLevelChangeRecordResponse;
import lombok.Data;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */

public interface IDangerLevelChangeRecordService {
    int changeLevel(DangerLevelRequest request);

    BaseResponse<List<DangerLevelChangeRecordResponse>> searchChangeLevelList(Long childrenId);
}
