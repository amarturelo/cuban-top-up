package com.wirelesskings.data.rxmail.async;


import com.wirelesskings.data.rxmail.settings.Setting;

public class Call {
    private Setting setting;
    private OnStateChangedListener changedListener;

    public Call(Setting setting, OnStateChangedListener changedListener) {
        this.setting = setting;
        this.changedListener = changedListener;
    }
}
