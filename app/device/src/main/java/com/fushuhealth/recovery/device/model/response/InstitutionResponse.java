package com.fushuhealth.recovery.device.model.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
@Data
public class InstitutionResponse {
    private Long id;
    private Long userId;
    private String name;
    private String userName;
    private Long institutionLevel;
    private Long parentId;
    private String contactNumber;
    private String doctor;
    private String address;
}
