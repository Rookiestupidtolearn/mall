package com.platform.exception;

public class GrowthTypeNullException extends MemberException {
    public GrowthTypeNullException() {
    }

    public GrowthTypeNullException(String message) {
        super(message);
    }

    public GrowthTypeNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrowthTypeNullException(Throwable cause) {
        super(cause);
    }
}
