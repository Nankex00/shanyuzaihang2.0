package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ScaleCode {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Byte code;

    private String name;

    private Byte firstLevel;

    private Byte secondLevel;

    private Byte status;

    private Long created;

    private Long updated;
}
