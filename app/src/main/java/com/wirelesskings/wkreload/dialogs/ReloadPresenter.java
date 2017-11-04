package com.wirelesskings.wkreload.dialogs;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.domain.interactors.ReloadsInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.ServerRepository;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadPresenter extends BasePresenter<ReloadContract.View>
        implements ReloadContract.Presenter {

    private ServerInteractor serverInteractor;

    public ReloadPresenter(ServerInteractor serverInteractor) {
        this.serverInteractor = serverInteractor;
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
    public void onReload(String wk_user, String wk_pass, String nauta_user, String client_name, String client_number, String reload_count, String reload_amount) {
        view.loading();
        addSubscription(serverInteractor.reload(wk_user, wk_pass, nauta_user, client_name, client_number, reload_count, reload_amount)
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(owner -> view.complete(), throwable -> view.error(throwable)));
    }

    public void cancel() {
        clearSubscriptions();
    }


}
