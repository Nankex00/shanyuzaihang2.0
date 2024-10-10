package com.fushuhealth.recovery.dal.vo;

import com.fushuhealth.recovery.dal.dto.GMsScaleEvaluationResult;
import lombok.Data;

@Data
public class GMsScaleRecordVo extends ScaleRecordVo{

    private Integer questionSn;

    private Long videoId;

    private String videoUrl;

    private String coverUrl;

    private GMsScaleEvaluationResult result;
}
