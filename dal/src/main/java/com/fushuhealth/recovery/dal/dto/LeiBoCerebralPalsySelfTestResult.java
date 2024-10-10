package com.fushuhealth.recovery.dal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fushuhealth.recovery.dal.dto.BaseScaleEvaluationResult;
import com.fushuhealth.recovery.dal.vo.SuggestVo;
import com.fushuhealth.recovery.dal.vo.VideoVo;
import lombok.Data;

import java.util.List;

@Data
public class LeiBoCerebralPalsySelfTestResult extends BaseScaleEvaluationResult {

    private Integer cerebralPalsyScore;

    private Boolean haveHighRisk;

    private Boolean haveAbnormalIterm;

    private List<String> highRisk;

    private List<String> abnormalIterm;

    private List<PositionAndSportIterm> positionAndSportAbnormal;

    private Integer modifyCount;

    private String remark;

    //蕾波建议
    private List<SuggestVo> leiboSuggest;

    private Boolean useLeiboSuggest = true;

    //用户写的建议
    private List<String> suggests;

    private String obviouslyBehind;

    private String tendencyBehind;

    private List<SuggestVo> suggest;

    private String hint;

    private List<VideoVo> videos;

    private Integer videoStatus;
}
