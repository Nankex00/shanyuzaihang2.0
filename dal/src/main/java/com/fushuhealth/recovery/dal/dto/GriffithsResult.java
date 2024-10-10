package com.fushuhealth.recovery.dal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fushuhealth.recovery.dal.vo.ScoringBookVo;
import lombok.Data;

import java.util.List;

@Data
public class GriffithsResult extends BaseScaleEvaluationResult {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private boolean draft = true;

    private List<Long> scoringBookFileIds;

    private List<String> scoringBookUrls;

    private List<ScoringBookVo> scoringBookVos;

    private String remark;

    private List<List<BaseTableResultCell>> table;
}
