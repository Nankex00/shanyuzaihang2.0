package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Actions {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String actionName;

    private Long drawingFileId;

    private Long photoFileId;

    private String instruction;

    private String precaution;

    private Integer videoCount;

    private Integer cameraCount;

    private String cameraPosition;

    private String analysisPosition;

    private String actionCode;

    private String motionId;

    private String matrix;

    private String remark;

    private Long userId;

    private String resultPage;

    private Byte status;

    private Long created;

    private Long updated;
}
