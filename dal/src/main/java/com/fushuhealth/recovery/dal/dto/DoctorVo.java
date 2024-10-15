package com.fushuhealth.recovery.dal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DoctorVo {

    private Long id;

    private String phone;

    private String email;

    private String name;

    private String avatar;

    private Byte gender;

    private Long organizationId;

    private Byte dataPermission;

    @JsonIgnore
    private Long doctorRegistrationId;

    @JsonIgnore
    private Long roleId;

    private String roleDesc;

    private String roleName;
}
