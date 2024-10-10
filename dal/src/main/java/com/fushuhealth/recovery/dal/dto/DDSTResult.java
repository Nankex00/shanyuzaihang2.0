package com.fushuhealth.recovery.dal.dto;

import lombok.Data;

@Data
public class DDSTResult extends BaseScaleEvaluationResult{

    // 1:合作, 2:不合作（合作绿色，不合作红色）
    private Integer cooperate;

    //1:正常（绿色），2:可疑（橙色），3:异常（红色），4:无法评定（黑色）
    private Integer result;

    private String suggest;

    private String remark = "该报告单仅反映本次评定结果";
}
