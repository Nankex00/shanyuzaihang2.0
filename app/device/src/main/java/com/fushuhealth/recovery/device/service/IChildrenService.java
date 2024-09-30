package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.device.model.request.ChildrenRequest;
import com.fushuhealth.recovery.device.model.response.ChildrenDetail;
import com.fushuhealth.recovery.device.model.response.ChildrenResponse;
import com.fushuhealth.recovery.device.model.response.SysDeptResponse;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
public interface IChildrenService {
    BaseResponse<List<ChildrenResponse>> searchList(ChildrenRequest request);

    ChildrenDetail searchDetail(Long id);
}
