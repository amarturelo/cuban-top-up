package com.wirelesskings.wkreload.activities;

import android.support.annotation.NonNull;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.wirelesskings.data.cache.OwnerCache;
import com.wirelesskings.data.cache.OwnerCacheImp;
import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.data.model.mapper.FatherDataMapper;
import com.wirelesskings.data.model.mapper.OwnerDataMapper;
import com.wirelesskings.data.model.mapper.PromotionDataMapper;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.ServerRepositoryImpl;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.executor.ThreadExecutor;
import com.wirelesskings.wkreload.domain.interactors.ServerConfigInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alberto on 18/10/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private ThreadExecutor threadExecutor;

    public LoginPresenter(ThreadExecutor threadExecutor) {
        this.threadExecutor = threadExecutor;
    }

    @Override
    public void bindView(@NonNull LoginContract.View view) {
        super.bindView(view);
    }

    @Override
    public void login(final ServerConfig serverConfig) {
        clearSubscriptions();

        final WKSDK wksdk = new WKSDK(serverConfig, new OwnerCacheImp());

        addSubscription(wksdk.update()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
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
                .subscribe(new Consumer<RealmOwner>() {
                               @Override
                               public void accept(RealmOwner realmOwner) throws Exception {
                                   if (!Boolean.valueOf(realmOwner.getNauta_active()))
                                       view.showError(new UserInactiveWKException());
                                   else {
                                       view.loginComplete();
                                   }
                                   loginDone(wksdk);
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

    private void loginDone(WKSDK wksdk) {
        WK.getInstance().replaceWKSession(wksdk);
    }

    public void cancel() {
        clearSubscriptions();
    }


}
