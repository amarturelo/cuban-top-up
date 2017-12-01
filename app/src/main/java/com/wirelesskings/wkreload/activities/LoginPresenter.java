package com.wirelesskings.wkreload.activities;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.executor.ThreadExecutor;
import com.wirelesskings.wkreload.domain.interactors.ServerConfigInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alberto on 18/10/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private ThreadExecutor threadExecutor;

    private ServerInteractor serverInteractor;

    public LoginPresenter(ThreadExecutor threadExecutor, ServerInteractor serverInteractor) {
        this.threadExecutor = threadExecutor;
        this.serverInteractor = serverInteractor;
    }

    @Override
    public void bindView(@NonNull LoginContract.View view) {
        super.bindView(view);
    }

    @Override
    public void update(String nauta_mail, String wk_username, String wk_password) {
        view.showLoading();
        Disposable subscription = serverInteractor.update(wk_username, wk_password, nauta_mail)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Owner>() {
                            @Override
                            public void accept(Owner owner) throws Exception {
                                view.hideLoading();
                                if (owner.getNauta_active().equals("true"))
                                    view.loginComplete();
                                else
                                    view.showError(new UserInactiveWKException());
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.showError((Exception) throwable);
                            }
                        });
        addSubscription(subscription);
    }

    public void cancel() {
        clearSubscriptions();
    }


}
