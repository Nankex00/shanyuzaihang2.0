package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.device.model.response.SettleRecordResponse;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
public interface ISettleRecordService {
    BaseResponse<List<SettleRecordResponse>> list(Long childId);
}
