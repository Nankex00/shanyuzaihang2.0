package com.fushuhealth.recovery.dal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class TrainingResultVo {

    private Long id;

    private Long recordId;

    private String name;

    private Integer age;

    private String gender;

    private String phone;

    private String actionName;

    private String trainingTime;

    private String featureFileUrl;

    private String keypointFileUrl;

    private String duration;

    private String actionCode;

    @JsonIgnore
    private String analysisPosition;

    private Integer imageCount;

    private List<PositionFileVo> srcVideoUrl;

    private List<VideoResultVo> resultVideoUrl;

    private String dstVideoUrl;

    private Integer cycles;

    private String remark;

    private String doctorName;

}
