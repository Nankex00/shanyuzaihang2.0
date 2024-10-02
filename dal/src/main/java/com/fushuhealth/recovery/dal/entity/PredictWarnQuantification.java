package com.fushuhealth.recovery.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictWarnQuantification {
    private Long id;
    private Byte monthAge;
    private String screeningItem;
    private Byte fillingForm;
    private String normalStandard;
    private String sceneDesign;
}
