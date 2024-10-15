package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

@Data
public class AttachmentRequest {

    private Byte type;

    private String serverId;
}