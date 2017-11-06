package com.wirelesskings.wkreload.dialogs;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.domain.interactors.OwnerInteractor;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ViewReloadPresenter extends BasePresenter<ViewReloadContract.View>
        implements ViewReloadContract.Presenter {

    private OwnerInteractor reloadsInteractor;

    public ViewReloadPresenter(OwnerInteractor reloadsInteractor) {
        this.reloadsInteractor = reloadsInteractor;
    }

    @Override
    public void bindView(@NonNull ViewReloadContract.View view) {
        super.bindView(view);
    }

    @Override
    public void onViewReload(String id) {
        view.showLoading();
        addSubscription(reloadsInteractor.reloadById(id)
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reload -> {
                    view.hideLoading();
                    view.renderReload(reload);
                }));
    }
}