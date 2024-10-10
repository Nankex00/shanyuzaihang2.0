package com.fushuhealth.recovery.dal.dto;

import lombok.Data;

import java.util.List;

@Data
public class LeiBoAllBodyTestResult extends BaseScaleEvaluationResult{

    private LeiBoCerebralPalsySelfTestResult cerebralPalsyResult;

    private GMsScaleEvaluationResult gmsResult;

    private Byte developmentRisk;
}
