package com.wirelesskings.wkreload.fragments;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.wirelesskings.data.cache.ClientCache;
import com.wirelesskings.data.cache.SellerCache;
import com.wirelesskings.data.cache.impl.ClientCacheImpl;
import com.wirelesskings.data.cache.impl.SellerCacheImpl;
import com.wirelesskings.data.model.RealmClient;
import com.wirelesskings.data.model.RealmSeller;
import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.model.FilterItemModel;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by alberto on 21/01/18.
 */

public class FilterDialogPresenter extends BasePresenter<FilterDialogContract.View>
        implements FilterDialogContract.Presenter {

    private ClientCache clientCache;
    private SellerCache sellerCache;

    public FilterDialogPresenter() {
        clientCache = new ClientCacheImpl();
        sellerCache = new SellerCacheImpl();
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
                                   view.renderClientName(Stream.of(realmClients)
                                           .map(new Function<RealmClient, FilterItemModel>() {
                                               @Override
                                               public FilterItemModel apply(RealmClient realmClient) {
                                                   return new FilterItemModel()
                                                           .setId(realmClient.getNumber())
                                                           .setText(realmClient.getName());
                                               }
                                           })
                                           .toList());

                                   view.renderClientNumbers(Stream.of(realmClients)
                                           .map(new Function<RealmClient, FilterItemModel>() {
                                               @Override
                                               public FilterItemModel apply(RealmClient realmClient) {
                                                   return new FilterItemModel()
                                                           .setId(realmClient.getNumber())
                                                           .setText(realmClient.getNumber());
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

    @Override
    public void onSellerName() {
        addSubscription(sellerCache.getAll()
                .firstOrError()
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .unsubscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RealmSeller>>() {
                    @Override
                    public void accept(List<RealmSeller> realmSellers) throws Exception {
                        view.renderSellerName(Stream.of(realmSellers)
                                .map(new Function<RealmSeller, FilterItemModel>() {
                                    @Override
                                    public FilterItemModel apply(RealmSeller realmSeller) {
                                        return new FilterItemModel()
                                                .setId(realmSeller.getName())
                                                .setText(realmSeller.getName());
                                    }
                                })
                                .toList());
                    }
                }));
    }

}
