package com.icodeshivam.statistics.model;

public enum ErrorCodes {

    MISSING_PARAMETER_AMOUNT(1001, "Parameter missing - amount"),
    MISSING_PARAMETER_TIMESTAMP(1002, "Parameter missing - timestamp"),
    INVALID_PARAMETER_AMOUNT(1003, "Parameter invalid - amount"),
    INVALID_PARAMETER_TIMESTAMP(1004, "Parameter invalid - amount"),
    CLIENT_SERVER_OUT_OF_SYNC(1005, "Client Server not in sync"),
    STALE_TIMESTAMP(1006, "Clients are too slow"),
    INTERNAL_SERVER_ERROR(9999, "Internal Server Error");

    private Integer errorCode;
    private String errorMessage;

    ErrorCodes(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
