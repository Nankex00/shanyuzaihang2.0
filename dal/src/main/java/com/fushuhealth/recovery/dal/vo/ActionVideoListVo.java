package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.util.List;

@Data
public class ActionVideoListVo {

    private Long id;

    private Long actionId;

    private String actionName;

    private Long userId;

    private String userName;

    private Byte isPublic;

    private String created;

    private String duration;

    private String coverUrl;

    private String sort;

//    private String url;

    private List<PositionFileVo> videos;

    private String analysisPostion;
}
