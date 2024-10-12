package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanAction {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long planId;

    private Long actionId;

    private Long actionVideoId;

    private Long videoDuration;

    private String actionName;

    private Integer cycles;

    private Integer sort;

    private Integer restTime;

    private Byte checkEnable;

    private Integer everyDuration;

    private Long actionCoverFileId;

    private Byte status;

    private Long created;

    private Long updated;
}
