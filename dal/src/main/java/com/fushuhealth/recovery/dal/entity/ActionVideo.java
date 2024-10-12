package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ActionVideo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String videoName;

    private Long actionId;

    private String videos;

    private Long coverFileId;

    private Long duration;

    private Long userId;

    private Byte isPublic;

    private Byte status;

    private Long created;

    private Long updated;
}
