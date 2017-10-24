package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.presenter.BaseContract;


public interface LoginContract {

    interface View extends BaseContract.View {

        void showServerConfig(ServerConfig serverConfig);
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void onHasCredentials();

        void findUpdate(String nauta_mail, String nauta_password, String wk_username, String wk_password);
    }
}
