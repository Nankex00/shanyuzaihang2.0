package com.fushuhealth.recovery.device.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fushuhealth.recovery.dal.entity.Diagnose;
import lombok.Data;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
public class DiagnoseResponse {
    private Long id;
    private String categoryName;
    private List<DiagnoseResponse> children;
}
