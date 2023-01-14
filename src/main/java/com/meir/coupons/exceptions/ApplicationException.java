package com.meir.coupons.exceptions;

import com.meir.coupons.enums.ErrorType;

public class ApplicationException extends Exception{

    private ErrorType errorType;

    public ApplicationException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public ApplicationException(Exception e, ErrorType errorType, String message) {
        super(message, e);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
