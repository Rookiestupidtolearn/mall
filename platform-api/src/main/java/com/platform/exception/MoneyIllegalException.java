package com.platform.exception;

public class MoneyIllegalException extends MemberException {
    public MoneyIllegalException() {
    }

    public MoneyIllegalException(String message) {
        super(message);
    }

    public MoneyIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoneyIllegalException(Throwable cause) {
        super(cause);
    }
}
