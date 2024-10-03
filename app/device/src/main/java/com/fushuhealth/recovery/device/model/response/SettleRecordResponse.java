package com.fushuhealth.recovery.device.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
@Schema(name = "SettleRecordResponse对象",description = "结案记录列表")
public class SettleRecordResponse {
    @Schema(name = "结案记录id")
    private Long id;
    @Schema(name = "操作机构")
    private String operatedDept;
    @Schema(name = "高危等级")
    private Byte dangerLevel;
    @Schema(name = "结案诊断")
    private String reason;
    @Schema(name = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date operatedTime;
}
