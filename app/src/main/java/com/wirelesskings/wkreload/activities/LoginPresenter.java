package com.wirelesskings.wkreload.activities;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.domain.executor.ThreadExecutor;
import com.wirelesskings.wkreload.domain.interactors.ServerConfigInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
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

    private ServerInteractor serverInteractor;

    public LoginPresenter(ThreadExecutor threadExecutor, ServerConfigInteractor serverConfigInteractor, ServerInteractor serverInteractor) {
        this.threadExecutor = threadExecutor;
        this.serverConfigInteractor = serverConfigInteractor;
        this.serverInteractor = serverInteractor;
    }

    public LoginPresenter(ThreadExecutor threadExecutor, ServerConfigInteractor serverConfigInteractor) {
        this.threadExecutor = threadExecutor;
        this.serverConfigInteractor = serverConfigInteractor;
    }

    public LoginPresenter setServerInteractor(ServerInteractor serverInteractor) {
        this.serverInteractor = serverInteractor;
        return this;
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
    public void login(String nauta_mail, String wk_username, String wk_password) {
        view.showLoading();
        Disposable subscription = serverInteractor.update(wk_username, wk_password, nauta_mail)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(owner -> {
                    view.hideLoading();
                    if (owner.getNauta_active().equals("true"))
                        view.loginComplete();
                    else
                        view.showError(new Exception("User not active"));
                });
        addSubscription(subscription);
    }

    public void cancel() {
        clearSubscriptions();
    }


}
