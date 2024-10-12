package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TrainingActionRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long recordId;

    private Long actionId;

    private Long instrumentId;

    private Long equipmentId;

    private Long userId;

    private Long doctorId;

    private Long startTime;

    private Long startDay;

    private Long endTime;

    private Long duration;

    private String videos;

    private Long coverFileId;

    private Byte trainingStatus;

    private Byte status;

    private Long created;

    private Long updated;
}