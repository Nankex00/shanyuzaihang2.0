package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ScaleVideoMark {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long recordId;

    private Long doctorId;

    private Byte scaleTableCode;

    private Integer scaleQuestionSn;

    private Long fileId;

    private String tag;

    private String startTime;

    private String endTime;

    private Byte source;

    private Byte status;

    private Long created;

    private Long updated;
}
