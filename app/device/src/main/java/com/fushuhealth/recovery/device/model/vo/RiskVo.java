package com.fushuhealth.recovery.device.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
@Data
public class RiskVo {

    private List<String> childRisk;

    private List<String> motherRisk;
}