package com.wirelesskings.wkreload.fragments;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.domain.interactors.ReloadsInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.model.mapper.ReloadItemDataMapper;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadsPresenter extends BasePresenter<ReloadsContract.View>
        implements ReloadsContract.Presenter {

    private ReloadsInteractor reloadsInteractor;

    private ReloadItemDataMapper reloadItemDataMapper;

    private ServerInteractor serverInteractor;


    public ReloadsPresenter(ReloadsInteractor reloadsInteractor, ReloadItemDataMapper reloadItemDataMapper, ServerInteractor serverInteractor) {
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
                WK.getInstance().getCredentials().getEmail(),
                WK.getInstance().getCredentials().getPassword(),
                WK.getInstance().getCredentials().getCredentials().getUsername())
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
    public void onDebit() {
        addSubscription(reloadsInteractor.debit()
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> view.renderDebit(aLong)));
    }

    @Override
    public void onReloads() {
        addSubscription(reloadsInteractor.reloads()
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reloadCollectionChange -> {
                    if (reloadCollectionChange.getInserted() != null)
                        view.renderInsertions(reloadItemDataMapper.transform(reloadCollectionChange.getInserted()));
                    if (reloadCollectionChange.getChanged() != null)
                        view.renderChanges(reloadItemDataMapper.transform(reloadCollectionChange.getChanged()));
                    if (reloadCollectionChange.getDeleted() != null)
                        view.renderDeletions(reloadItemDataMapper.transform(reloadCollectionChange.getDeleted()));
                }));
    }

    @Override
    public void bindView(@NonNull ReloadsContract.View view) {
        super.bindView(view);
        onReloads();
        onDebit();
    }
}
