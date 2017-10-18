package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.presenter.BaseContract;


public interface LoginContract {

    interface View extends BaseContract.View {

        void hasServerSettings();

        void showHome();

        void showLogin();

        void showServerSettings();
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void onServerSettings();

    }
}
