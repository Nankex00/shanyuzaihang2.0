package com.fushuhealth.recovery.device.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class VideoMarkParam {

    private String name;

    private List<VideoMarkParam> child;
}
