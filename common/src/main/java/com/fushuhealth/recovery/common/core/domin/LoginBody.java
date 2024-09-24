package com.fushuhealth.recovery.common.core.domin;

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
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;
}
