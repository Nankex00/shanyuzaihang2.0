package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ActionVo {

    private Long id;

    private String actionName;

    private String coverUrl;

    @JsonIgnore
    private Long coverFileId;

    private String type;

    private String url;

    private List<String> cameraPosition;

    @JsonIgnore
    private String analysisPosition;

    private String actionCode;

    private Integer cameraCount;

    private String remark;

    @JsonIgnore
    private Long instrumentId;

    private String instrument;

    @JsonIgnore
    private Long equipmentId;

    private String equipment;

    @JsonIgnore
    private String resultPage;

    @JsonIgnore
    private String motionId;

    private Long tagId;
}
