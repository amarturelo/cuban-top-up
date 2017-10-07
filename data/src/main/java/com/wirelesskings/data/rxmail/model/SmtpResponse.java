package com.wirelesskings.data.rxmail.model;

public class SmtpResponse {
    private String errorDescription;
    private int errorCode;

    public SmtpResponse() {
        this.errorCode = 0;
        this.errorDescription = "";
    }

    /**
     * @param errorCode
     * @param errorDescription
     */
    public SmtpResponse(int errorCode, String errorDescription ) {
        this.errorDescription = errorDescription;
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
