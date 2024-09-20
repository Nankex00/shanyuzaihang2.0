package com.fushuhealth.recovery.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Result Code Enum
 *
 * @author william
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(0, "操作成功"),

    USER_NOT_FOUND(1, "用户不存在"),

    USER_EXIST(1, "用户已存在"),

    PASSWORD_ERROR(1, "密码错误"),

    USER_DISABLED(1, "用户已禁用"),

    USER_NOT_LOGIN(2, "用户未登陆"),

    PERMISSION_ERROR(1, "权限错误"),

    NOT_FOUND(1, "没有找到"),

    INTERNAL_SERVER_ERROR(1, "内部错误"),

    PARAM_ERROR(1, "参数错误"),

    UNSUPPORTED_FILE_TYPE(1, "不支持的文件类型");

    final int code;

    final String msg;
}
