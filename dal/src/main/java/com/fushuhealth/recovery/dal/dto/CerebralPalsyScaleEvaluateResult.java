package com.fushuhealth.recovery.dal.dto;

import com.fushuhealth.recovery.dal.dto.BaseScaleEvaluationResult;
import com.fushuhealth.recovery.dal.vo.SuggestVo;
import lombok.Data;

import java.util.List;

@Data
public class CerebralPalsyScaleEvaluateResult extends BaseScaleEvaluationResult {

    private String result;

    private String remark;

    private List<String> highRisk;

    private List<String> abnormalIterm;

    private List<SuggestVo> suggest;

    private String productId;

    private String resourceId;
}
