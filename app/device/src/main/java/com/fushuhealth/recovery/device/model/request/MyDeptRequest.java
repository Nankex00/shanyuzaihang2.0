package com.fushuhealth.recovery.device.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
public class MyDeptRequest {
    @NotBlank(message = "机构名称不能为空")
    private String deptName;
    @NotBlank(message = "登录账号不能为空")
    private String userName;
    @NotBlank(message = "登录密码不能为空")
    private String password;
    /**
     * 默认管理员权限为1，市级机构为2，区县级为3，社区级为4
     */
    @NotNull(message = "机构等级不能为空")
    private Long institutionLevel;
    @NotNull(message = "父机构id不能为空")
    private Long parentId;
    private String contactNumber;
    private String doctor;
    private String address;
}
