package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TrainingPlanTemplate {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer actionCount;

    private String actions;

    private Long userId;

    private Byte status;

    private Long created;

    private Long updated;
}
