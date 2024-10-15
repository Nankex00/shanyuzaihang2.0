package com.fushuhealth.recovery.h5.service;


import com.fushuhealth.recovery.dal.vo.h5.ScaleEvaluateLogListVo;

import java.util.List;

public interface ScaleEvaluateLogService {

    List<ScaleEvaluateLogListVo> listScaleEvaluateLogByScaleRecordId(long recordId);
}
