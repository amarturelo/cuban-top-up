package com.wirelesskings.wkreload.activities;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.CacheHelper;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.executor.ThreadExecutor;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alberto on 18/10/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private ThreadExecutor threadExecutor;

    private CacheHelper cacheHelper;

    public LoginPresenter(ThreadExecutor threadExecutor, CacheHelper cacheHelper) {
        this.threadExecutor = threadExecutor;
        this.cacheHelper = cacheHelper;
    }

    @Override
    public void bindView(@NonNull LoginContract.View view) {
        super.bindView(view);
    }

    @Override
    public void login(final ServerConfig serverConfig) {
        clearSubscriptions();

        final WKSDK wksdk = new WKSDK(serverConfig);

        addSubscription(wksdk.update()
                .doOnSuccess(new Consumer<WKSDK.WKOwner>() {
                    @Override
                    public void accept(WKSDK.WKOwner wkOwner) throws Exception {
                        cacheHelper.save(wkOwner, serverConfig.getCredentials().getUsername());
                    }
                })
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        view.showLoading();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();

                    }
                })
                .subscribe(new Consumer<WKSDK.WKOwner>() {
                               @Override
                               public void accept(WKSDK.WKOwner realmOwner) throws Exception {
                                   if (!Boolean.valueOf(realmOwner.getNauta_active()))
                                       view.showError(new UserInactiveWKException());
                                   else
                                       view.loginComplete();
                                   wksdk.getServerConfig().setActive(Boolean.parseBoolean(realmOwner.getNauta_active()));
                                   requestDone(wksdk);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.showError((Exception) throwable);
                            }
                        })
        );
    }

    private void requestDone(WKSDK wksdk) {
        WK.getInstance().replaceWKSession(wksdk);
    }

    public void cancel() {
        clearSubscriptions();
    }

}
