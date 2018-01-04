package com.wirelesskings.wkreload.activities;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.CacheHelper;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.executor.ThreadExecutor;
import com.wirelesskings.wkreload.model.PreReloadItemModel;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by alberto on 1/01/18.
 */

public class ReloadPresenter extends BasePresenter<ReloadContract.View>
        implements ReloadContract.Presenter {

    private WKSDK wksdk;
    private ThreadExecutor threadExecutor;

    private CacheHelper cacheHelper;


    public ReloadPresenter(WKSDK wksdk, ThreadExecutor threadExecutor, CacheHelper cacheHelper) {
        this.wksdk = wksdk;
        this.threadExecutor = threadExecutor;
        this.cacheHelper = cacheHelper;
    }

    @Override
    public void reload(List<PreReloadItemModel> reloadItemModels) {
        addSubscription(wksdk.reload(Stream.of(reloadItemModels)
                .map(new Function<PreReloadItemModel, WKSDK.ReloadParams>() {
                    @Override
                    public WKSDK.ReloadParams apply(PreReloadItemModel preReloadItemModel) {
                        return new WKSDK.ReloadParams()
                                .setName(preReloadItemModel.getClientName())
                                .setNumber(preReloadItemModel.getClientNumber())
                                .setCount(preReloadItemModel.getCount())
                                .setAmount(preReloadItemModel.getAmount());
                    }
                })
                .toList())
                .doOnSuccess(new Consumer<WKSDK.WKOwner>() {
                    @Override
                    public void accept(WKSDK.WKOwner wkOwner) throws Exception {
                        cacheHelper.save(wkOwner, wksdk.getServerConfig().getCredentials().getUsername());
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
                                   wksdk.getServerConfig().setActive(realmOwner.getNauta_active().equals("1"));
                                   requestDone(wksdk);
                                   if (!realmOwner.getNauta_active().equals("1"))
                                       view.showError(new UserInactiveWKException());
                                   else
                                       view.reloadComplete();

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
}
