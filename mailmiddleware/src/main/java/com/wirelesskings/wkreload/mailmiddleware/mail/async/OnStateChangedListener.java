package com.wirelesskings.wkreload.mailmiddleware.mail.async;

import java.util.ArrayList;

public interface OnStateChangedListener {
    void onExecuting();
    void onSuccess(ArrayList<?> list);
    void onError(int code, String msg);
    void onCanceled();
}
