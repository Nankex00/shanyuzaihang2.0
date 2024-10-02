package com.fushuhealth.recovery.device.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
@AllArgsConstructor
public class EvaluateRecordDetail {
    private Long id;
    private Long childId;
    private Byte monthAge;
    private String geSellResult;
    private String geSellUrls;
    private String sSResult;
    private String sSUrls;
    private String otherResult;
    private String otherUrls;
}
