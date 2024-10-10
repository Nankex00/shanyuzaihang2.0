package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Files {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String rawName;

    private String originalName;

    private String extension;

    private String filePath;

    private String rotatePath;

    private Byte fileType;

    private Long fileSize;

    private Byte status;

    private Long created;

    private Long updated;
}
