package com.fushuhealth.recovery.device.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class LoginVo {

    private Long id;

    private String phone;

    private Integer age;

    private String email;

    private String name;

    private String avatar;

    private Byte gender;

    private Long organizationId;

    private String organizationName;

    private Boolean uploadVideo;

    @JsonIgnore
    private String appId;

    @JsonIgnore
    private String openId;

    @JsonIgnore
    private Long roleId;

    private String roleDesc;

    private String roleName;

    private Boolean showBrain;

    private Byte reportType;

    @JsonIgnore
    private List<Long> actionIds;
}
