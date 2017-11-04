package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.presenter.BaseContract;


public interface LoginContract {

    interface View extends BaseContract.View {

        void loginComplete();

        void showLoading();

        void hideLoading();

        void showError(Exception e);
    }

    interface Presenter extends BaseContract.Presenter<View> {


        void update(String nauta_mail, String wk_username, String wk_password, String token);

    }
}
