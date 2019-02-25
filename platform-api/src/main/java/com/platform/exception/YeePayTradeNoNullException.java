package com.platform.exception;

public class YeePayTradeNoNullException extends MemberException {
    public YeePayTradeNoNullException() {
    }

    public YeePayTradeNoNullException(String message) {
        super(message);
    }

    public YeePayTradeNoNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public YeePayTradeNoNullException(Throwable cause) {
        super(cause);
    }
}
