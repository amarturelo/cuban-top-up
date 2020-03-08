package com.wirelesskings.wkreload.mailmiddleware;

import com.wirelesskings.wkreload.mailmiddleware.Listener;

/**
 * Created by Alberto on 01/11/2017.
 */

public interface SuccessListened extends Listener {
    void onSuccess();

    void onError(Exception e);
}
