package com.wirelesskings.wkreload.mail.async;

import java.util.ArrayList;

public interface OnStateChangedListener {
    void onExecuting();
    void onSuccess(ArrayList<?> list);
    void onError(int code, String msg);
    void onCanceled();
}
