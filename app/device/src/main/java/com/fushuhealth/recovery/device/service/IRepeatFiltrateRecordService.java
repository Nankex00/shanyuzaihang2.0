package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.dal.entity.RepeatFiltrateRecord;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateEditRequest;
import com.fushuhealth.recovery.device.model.request.RepeatFiltrateRecordRequest;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordDetail;
import com.fushuhealth.recovery.device.model.response.RepeatFiltrateRecordResponse;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
public interface IRepeatFiltrateRecordService {
    int addRepeatFiltrateRecord(RepeatFiltrateRecordRequest request);

    BaseResponse<List<RepeatFiltrateRecordResponse>> searchListByChildId(Long childId);

    RepeatFiltrateRecordDetail searchDetail(Long id);

    int editDetail(RepeatFiltrateEditRequest request);

    int delete(Long id);
}
