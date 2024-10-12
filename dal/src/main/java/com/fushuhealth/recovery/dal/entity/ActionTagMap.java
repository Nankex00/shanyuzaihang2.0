package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ActionTagMap {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long actionId;

    private Long tagId;
}
