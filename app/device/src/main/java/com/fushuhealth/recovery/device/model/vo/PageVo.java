package com.fushuhealth.recovery.device.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageVo {
    private long page;

    private long totalPage;

    private long totalRecord;
}
