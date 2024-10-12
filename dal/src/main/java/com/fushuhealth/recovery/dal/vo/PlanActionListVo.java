package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class PlanActionListVo {

    private Long id;

    private Long actionId;

    private String actionName;

    private Integer cycles;

    private Boolean check;

    private Integer everyDuration;

    private String actionCoverUrl;

    private String actionVideoUrl;

    @JsonIgnore
    private Long videoDuration;

    private Integer order;

    private Integer restTime;

    private List<PositionFileVo> videos;
}
