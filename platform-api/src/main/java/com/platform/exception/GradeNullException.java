package com.platform.exception;

public class GradeNullException extends MemberException {

    public GradeNullException() {
    }

    public GradeNullException(String message) {
        super(message);
    }

    public GradeNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public GradeNullException(Throwable cause) {
        super(cause);
    }
}
