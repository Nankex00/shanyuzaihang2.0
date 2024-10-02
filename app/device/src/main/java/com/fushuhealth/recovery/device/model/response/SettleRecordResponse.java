package com.fushuhealth.recovery.device.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
@ApiModel(value = "SettleRecordResponse对象",description = "结案记录列表")
public class SettleRecordResponse {
    @ApiModelProperty(value = "结案记录id")
    private Long id;
    @ApiModelProperty(value = "操作机构")
    private String operatedDept;
    @ApiModelProperty(value = "高危等级")
    private Byte dangerLevel;
    @ApiModelProperty(value = "结案诊断")
    private String reason;
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date operatedTime;
}
