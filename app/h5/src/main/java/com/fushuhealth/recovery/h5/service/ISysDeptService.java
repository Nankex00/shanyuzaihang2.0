package com.fushuhealth.recovery.h5.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.ReportType;
import com.fushuhealth.recovery.common.constant.Dict;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
public interface ISysDeptService {

    ReportType getReportType(long id);

    String selectDeptNameById(Long institutionId);

}
