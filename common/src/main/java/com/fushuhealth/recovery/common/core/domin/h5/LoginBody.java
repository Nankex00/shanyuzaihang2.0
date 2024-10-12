package com.fushuhealth.recovery.common.core.domin.h5;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Zhuanz
 * @date 2024/9/23
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    @NotNull(message = "手机号不能为空")
    private String username;

    /**
     * 验证码
     */
    @NotNull(message = "验证码不能为空")
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;
}
