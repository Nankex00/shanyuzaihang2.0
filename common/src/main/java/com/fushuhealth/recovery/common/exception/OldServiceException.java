package com.fushuhealth.recovery.common.exception;

import com.fushuhealth.recovery.common.api.ResultCode;
import lombok.Getter;

/**
 * Business Service Exception
 *
 * @author william
 */
public class OldServiceException extends RuntimeException {
    private static final long serialVersionUID = 2359767895161832954L;

    @Getter
    private final ResultCode resultCode;

    public OldServiceException(String message) {
        super(message);
        this.resultCode = ResultCode.INTERNAL_SERVER_ERROR;
    }

    public OldServiceException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public OldServiceException(ResultCode resultCode, String msg) {
        super(msg);
        this.resultCode = resultCode;
    }

    public OldServiceException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
    }

    public OldServiceException(String msg, Throwable cause) {
        super(msg, cause);
        this.resultCode = ResultCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * for better performance
     *
     * @return Throwable
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }
}
