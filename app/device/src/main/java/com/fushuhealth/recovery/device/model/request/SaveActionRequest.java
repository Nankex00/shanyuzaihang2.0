package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

@Data
public class SaveActionRequest {

    private String name;

    private Long tagId;

    private Integer cameraCount;

    private String[] cameraPosition;

    private String actionCode;

    private String remark;

    private CommitUploadRequest fileInfo;
}
