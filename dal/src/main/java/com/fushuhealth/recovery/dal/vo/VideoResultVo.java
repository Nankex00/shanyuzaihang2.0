package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

@Data
public class VideoResultVo extends PositionFileVo {

    private String featureFileUrl;

    private String keypointFileUrl;
}
