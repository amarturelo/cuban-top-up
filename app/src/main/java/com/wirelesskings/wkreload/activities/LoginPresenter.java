package com.wirelesskings.wkreload.activities;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.domain.executor.ThreadExecutor;
import com.wirelesskings.wkreload.domain.interactors.ServerConfigInteractor;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alberto on 18/10/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private ThreadExecutor threadExecutor;

    private ServerConfigInteractor serverConfigInteractor;

    public LoginPresenter(ThreadExecutor threadExecutor, ServerConfigInteractor serverConfigInteractor) {
        this.threadExecutor = threadExecutor;
        this.serverConfigInteractor = serverConfigInteractor;
    }

    @Override
    public void bindView(@NonNull LoginContract.View view) {
        super.bindView(view);
        this.onHasCredentials();
    }

    @Override
    public void onHasCredentials() {
        final Disposable subscription = serverConfigInteractor.getServerConfig()
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(serverConfig -> view.showServerConfig(serverConfig));

        addSubscription(subscription);
    }

    @Override
    public void findUpdate(String nauta_mail, String nauta_password, String wk_username, String wk_password) {

    }
}
