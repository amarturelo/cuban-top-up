package com.wirelesskings.wkreload.fragments;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.domain.interactors.ReloadsInteractor;
import com.wirelesskings.wkreload.model.mapper.ReloadItemDataMapper;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadsPresenter extends BasePresenter<ReloadsContract.View>
        implements ReloadsContract.Presenter {

    private ReloadsInteractor reloadsInteractor;

    private ReloadItemDataMapper reloadItemDataMapper;

    public ReloadsPresenter(ReloadsInteractor reloadsInteractor, ReloadItemDataMapper reloadItemDataMapper) {
        this.reloadsInteractor = reloadsInteractor;
        this.reloadItemDataMapper = reloadItemDataMapper;
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
    }
}
