package com.fushuhealth.recovery.device.model.request;

import com.fushuhealth.recovery.device.model.request.CommitUploadRequest;
import lombok.Data;

import java.util.List;

@Data
public class SaveActionVideoRequest {

    private Long actionId;

    private Long duration;

    private Byte isPublic;

    private List<CommitUploadRequest> files;
}
