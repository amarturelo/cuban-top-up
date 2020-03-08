package com.wirelesskings.wkreload.mailmiddleware.callback;

/**
 * Created by Alberto on 24/10/2017.
 */

public interface Callback {
    void onError(Exception error);

    void onSuccess();
}
