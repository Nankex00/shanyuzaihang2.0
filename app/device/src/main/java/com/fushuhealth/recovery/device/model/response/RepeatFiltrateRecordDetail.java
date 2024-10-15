package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.dal.dto.FileDetailDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
    private List<FileDetailDto> aqsUrls;
    private String ddstResult;
    private List<FileDetailDto> ddstUrls;
    private String otherResult;
    private List<FileDetailDto> otherUrls;
}
