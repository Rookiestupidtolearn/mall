package com.platform.exception;

public class MemberNullException extends MemberException {
    public MemberNullException() {
    }

    public MemberNullException(String message) {
        super(message);
    }

    public MemberNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNullException(Throwable cause) {
        super(cause);
    }
}
