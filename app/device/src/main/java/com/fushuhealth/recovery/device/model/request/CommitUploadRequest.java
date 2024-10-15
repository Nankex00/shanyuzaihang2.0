package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

@Data
public class CommitUploadRequest {

    private String position;

    private String bucket;

    private String key;

    private Long size;

    private String fileName;
}
