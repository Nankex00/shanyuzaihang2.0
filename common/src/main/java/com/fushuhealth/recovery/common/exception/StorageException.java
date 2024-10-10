package com.fushuhealth.recovery.common.exception;

public class StorageException extends RuntimeException {

    public StorageException(Throwable e) {
        super(e);
    }

    public StorageException(String msg) {
        super(msg);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
