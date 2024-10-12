package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TrainingRecordResult {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long trainingRecordId;

    private Long trainingActionRecordId;

    private String position;

    private Long featureFileId;

    private Long keyPointsFileId;

    private Long srcVideoFileId;

    private Long dstVideoFileId;

    private String pictures;

    private Integer imageCount;

    private Integer finishCount;

    private Integer effectCount;

    private Long doctorId;

    private String doctorName;

    private String remark;

    private Byte status;

    private Long created;

    private Long updated;
}
