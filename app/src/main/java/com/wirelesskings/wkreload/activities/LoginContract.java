package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.presenter.BaseContract;

import io.reactivex.disposables.Disposable;


public interface LoginContract {

    interface View extends BaseContract.View {

        void loginComplete();

        void showLoading(Disposable disposable);

        void hideLoading();

        void showError(Exception e);
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void login(ServerConfig serverConfig);
    }
}
