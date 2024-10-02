package com.fushuhealth.recovery.device.model.response;

import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class RepeatFiltrateRecordDetail {
    private Long id;
    private Long childId;
    private Byte monthAge;
    private String aqsResult;
    private String aqsUrls;
    private String ddstResult;
    private String ddstUrls;
    private String otherResult;
    private String otherUrls;
}
