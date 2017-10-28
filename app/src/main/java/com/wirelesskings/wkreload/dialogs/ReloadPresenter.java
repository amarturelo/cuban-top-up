package com.wirelesskings.wkreload.dialogs;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.domain.interactors.ReloadsInteractor;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadPresenter extends BasePresenter<ReloadContract.View>
        implements ReloadContract.Presenter {

    private ReloadsInteractor reloadsInteractor;

    public ReloadPresenter(ReloadsInteractor reloadsInteractor) {
        this.reloadsInteractor = reloadsInteractor;
    }

    @Override
    public void bindView(@NonNull ReloadContract.View view) {
        super.bindView(view);
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public void onReload(String clientName, String clintNumber, int amount, int count) {
        addSubscription(reloadsInteractor.reload(clientName, clintNumber, amount, count)
                .doOnSubscribe(disposable -> view.loading())
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.complete(), throwable -> view.error(throwable)));
    }


}
