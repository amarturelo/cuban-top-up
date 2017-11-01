package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.presenter.BaseContract;


public interface LoginContract {

    interface View extends BaseContract.View {

        void showServerConfig(ServerConfig serverConfig);

        void onLoginSend(String id);

        void loginComplete();
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void onHasCredentials();

        void login(String nauta_mail, String wk_username, String wk_password);

        void onResult(String result);
    }
}
