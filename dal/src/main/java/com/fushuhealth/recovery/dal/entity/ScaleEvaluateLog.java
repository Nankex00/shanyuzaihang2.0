package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ScaleEvaluateLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long scaleRecordId;

    private Long userId;

    private Byte scaleStatus;

    private String remark;

    private Byte status;

    private Long created;

    private Long updated;
}
