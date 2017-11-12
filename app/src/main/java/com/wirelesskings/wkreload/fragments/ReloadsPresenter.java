package com.wirelesskings.wkreload.fragments;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.domain.interactors.OwnerInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.model.mapper.ReloadItemDataMapper;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadsPresenter extends BasePresenter<ReloadsContract.View>
        implements ReloadsContract.Presenter {

    private OwnerInteractor reloadsInteractor;

    private ReloadItemDataMapper reloadItemDataMapper;

    private ServerInteractor serverInteractor;


    public ReloadsPresenter(OwnerInteractor reloadsInteractor, ReloadItemDataMapper reloadItemDataMapper, ServerInteractor serverInteractor) {
        this.reloadsInteractor = reloadsInteractor;
        this.reloadItemDataMapper = reloadItemDataMapper;
        this.serverInteractor = serverInteractor;
    }

    @Override
    public void cancel() {
        view.hideLoading();
        clearSubscriptions();
    }

    @Override
    public void update() {
        view.showLoading();
        Disposable subscription = serverInteractor.update(
                WK.getInstance().getCredentials().getCredentials().getUsername(),
                WK.getInstance().getCredentials().getCredentials().getPassword(),
                WK.getInstance().getCredentials().getEmail())
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(owner -> {
                            view.hideLoading();
                            if (owner.getNauta_active().equals("true"))
                                view.updateComplete();
                            else
                                view.showError(new Exception("User not active"));
                        },
                        throwable -> view.showError((Exception) throwable));
        addSubscription(subscription);
    }

    @Override
    public void onReloads() {
        addSubscription(reloadsInteractor.owner()
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(owner -> {
                    if (owner.getPromotion() != null) {
                        view.hasPromotions(true);
                        view.renderInsertions(reloadItemDataMapper.transform(owner.getPromotion().getReloads()));
                    }
                    else
                        view.hasPromotions(false);
                    view.renderFather(owner.getFather());
                }));
    }

    @Override
    public void bindView(@NonNull ReloadsContract.View view) {
        super.bindView(view);
        onReloads();
    }
}
