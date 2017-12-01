package com.wirelesskings.wkreload.mailmiddleware.exceptions;

/**
 * Created by alberto on 30/11/17.
 */

public class NetworkErrorToSendException extends NetworkErrorException {
    public NetworkErrorToSendException(Throwable cause) {
        super(cause);
    }
}
