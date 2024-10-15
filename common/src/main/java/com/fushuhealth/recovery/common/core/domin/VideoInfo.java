package com.fushuhealth.recovery.common.core.domin;

import lombok.Data;

import java.io.File;

@Data
public class VideoInfo {

    private String format;

    private Integer duration;

    private Integer width;

    private Integer height;

    private File cover;
}
