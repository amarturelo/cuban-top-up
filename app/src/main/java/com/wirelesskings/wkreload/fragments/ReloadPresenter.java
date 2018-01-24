package com.wirelesskings.wkreload.fragments;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.wirelesskings.data.cache.ClientCache;
import com.wirelesskings.data.cache.impl.ClientCacheImpl;
import com.wirelesskings.data.model.RealmClient;
import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.model.FilterItemModel;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by alberto on 23/01/18.
 */

public class ReloadPresenter extends BasePresenter<ReloadContract.View>
        implements ReloadContract.Presenter {

    private ClientCache clientCache;

    public ReloadPresenter() {
        clientCache = new ClientCacheImpl();
    }

    @Override
    public void onClients() {
        addSubscription(clientCache.getAll()
                .firstOrError()
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .unsubscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RealmClient>>() {
                               @Override
                               public void accept(List<RealmClient> realmClients) throws Exception {
                                   view.renderClientNumbers(Stream.of(realmClients)
                                           .map(new Function<RealmClient, String>() {
                                               @Override
                                               public String apply(RealmClient realmClient) {
                                                   return realmClient.getNumber();
                                               }
                                           })
                                           .toList());
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.showError((Exception) throwable);
                            }
                        }));
    }
}
