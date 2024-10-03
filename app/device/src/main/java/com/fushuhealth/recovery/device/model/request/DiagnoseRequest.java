package com.fushuhealth.recovery.device.model.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class DiagnoseRequest {
    private List<Long> beforeDiagnose;
    private List<Long> diagnoseDetail;
    private Long childId;
}
