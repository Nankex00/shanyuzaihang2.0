package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ActionListVo {

    private Long id;

    private String actionName;

    private String coverUrl;

    @JsonIgnore
    private Long coverFileId;

    private String type;

    private String url;

    @JsonIgnore
    private Long fileId;

    private String created;

    @JsonIgnore
    private Long instrumentId;

    private String instrument;

    @JsonIgnore
    private Long equipmentId;

    private String equipment;
}
