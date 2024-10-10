package com.fushuhealth.recovery.dal.vo;

import com.fushuhealth.recovery.dal.dto.BaseScaleEvaluationResult;
import lombok.Data;

@Data
public class CerebralPalsyRecordVo extends ScaleRecordVo {

    private BaseScaleEvaluationResult result;
}
