package com.platform.exception;

public class MonetaryNullException extends MemberException {
    public MonetaryNullException() {
    }

    public MonetaryNullException(String message) {
        super(message);
    }

    public MonetaryNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public MonetaryNullException(Throwable cause) {
        super(cause);
    }
}
