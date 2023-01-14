package com.meir.coupons.exceptions;

public class ErrorBean {
    private int httpStatus;
    private String errorMessage;
    private String errorName;

    public ErrorBean(int httpStatus, String errorMessage, String errorName) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.errorName = errorName;
    }

    public ErrorBean(int httpStatus, String errorName) {
        this.httpStatus = httpStatus;
        this.errorName = errorName;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    @Override
    public String toString() {
        return "ErrorBean{" +
                "errorNumber=" + httpStatus +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorName='" + errorName + '\'' +
                '}';
    }
}
