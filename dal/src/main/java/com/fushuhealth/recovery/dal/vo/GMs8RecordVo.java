package com.fushuhealth.recovery.dal.vo;

import com.fushuhealth.recovery.dal.dto.GMs8Result;
import lombok.Data;

@Data
public class GMs8RecordVo extends ScaleRecordVo {

    private Long videoId;

    private String videoUrl;

    private String coverUrl;

    private GMs8Result result;
}
