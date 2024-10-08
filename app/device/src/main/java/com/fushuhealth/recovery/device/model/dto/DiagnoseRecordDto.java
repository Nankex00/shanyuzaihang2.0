package com.fushuhealth.recovery.device.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/10/8
 */
@Data
public class DiagnoseRecordDto {
    private Long id;
    private Long operatedId;
    private Date operatedTime;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> beforeDiagnose;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> diagnoseDetail;
    private Long childId;
}
