package com.wirelesskings.wkreload.mailmiddleware.mail.async;


import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

public class Call {
    private Setting setting;
    private OnStateChangedListener changedListener;

    public Call(Setting setting, OnStateChangedListener changedListener) {
        this.setting = setting;
        this.changedListener = changedListener;
    }
}
