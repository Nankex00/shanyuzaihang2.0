package com.fushuhealth.recovery.common.error;

import com.fushuhealth.recovery.common.api.ResultCode;
import lombok.Getter;

public class NotLoginException extends RuntimeException {

    @Getter
    private final ResultCode resultCode;

    public NotLoginException(String message) {
        super(message);
        this.resultCode = ResultCode.USER_NOT_LOGIN;
    }

    public NotLoginException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public NotLoginException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
