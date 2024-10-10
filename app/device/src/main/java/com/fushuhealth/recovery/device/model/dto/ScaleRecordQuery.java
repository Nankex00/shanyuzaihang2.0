package com.fushuhealth.recovery.device.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScaleRecordQuery {

    private String keyword;

    private Byte scaleCode;

    private Long start;

    private Long end;

    private Long userId;

    private Byte status;
}
