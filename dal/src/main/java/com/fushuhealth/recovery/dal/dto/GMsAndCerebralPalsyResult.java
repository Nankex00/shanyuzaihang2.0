package com.fushuhealth.recovery.dal.dto;

import lombok.Data;

@Data
public class GMsAndCerebralPalsyResult extends BaseScaleEvaluationResult{

    private GMsScaleEvaluationResult gmsResult;

    private CerebralPalsyScaleEvaluateResult cerebralPalsyResult;
}
