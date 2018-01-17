package com.wirelesskings.data.filter;

/**
 * Created by alberto on 17/01/18.
 */

public class ClientNumberFilter implements Filter {
    private String clientNumber;

    public ClientNumberFilter() {
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public ClientNumberFilter setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
        return this;
    }
}
