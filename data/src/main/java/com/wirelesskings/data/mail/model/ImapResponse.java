package com.wirelesskings.data.mail.model;

import java.util.ArrayList;

public class ImapResponse {
    private String errorDescription;
    private int errorCode;
    private ArrayList<Email> emails;

    public ImapResponse() {
        this.errorCode = 0;
        this.errorDescription = "";
    }

    public ArrayList<Email> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<Email> emails) {
        this.emails = emails;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
