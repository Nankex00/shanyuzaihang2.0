package com.fushuhealth.recovery.device.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDeptBo {

    private Long id;
    @NotNull(message = "用户id不能为空")
    private Long userId;
    @NotBlank(message = "机构名称不能为空")
    private String name;
    @NotBlank(message = "登录账号不能为空")
    private String userName;
    @NotBlank(message = "登录密码不能为空")
    private String password;
    @NotNull(message = "机构等级不能为空")
    private Long institutionLevel;
    @NotNull(message = "父机构id不能为空")
    private Long parentId;
    private String contactNumber;
    private String doctor;
    private String address;
}
