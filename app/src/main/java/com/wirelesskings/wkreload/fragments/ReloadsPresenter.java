package com.wirelesskings.wkreload.fragments;

import android.support.annotation.NonNull;

import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.interactors.OwnerInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.model.mapper.ReloadItemDataMapper;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadsPresenter extends BasePresenter<ReloadsContract.View>
        implements ReloadsContract.Presenter {

    private OwnerInteractor reloadsInteractor;

    private ReloadItemDataMapper reloadItemDataMapper;

    private Consumer<Owner> success = new Consumer<Owner>() {
        @Override
        public void accept(Owner owner) throws Exception {
            view.hideLoading();
            if (owner.getPromotion() != null) {
                view.renderInsertions(reloadItemDataMapper.transform(owner.getPromotion().getReloads()));
            } else
                view.showError(new Exception("No hay promociones en estos momentos"));
            view.renderFather(owner.getFather());
        }
    };

    private Consumer<Throwable> error = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            view.hideLoading();
            view.showError((Exception) throwable);
        }
    };

    private WKSDK wksdk;


    public ReloadsPresenter(OwnerInteractor reloadsInteractor, ReloadItemDataMapper reloadItemDataMapper, WKSDK wksdk) {
        this.reloadsInteractor = reloadsInteractor;
        this.reloadItemDataMapper = reloadItemDataMapper;
        this.wksdk = wksdk;
    }

    @Override
    public void cancel() {
        view.hideLoading();
        clearSubscriptions();
    }

    @Override
    public void update() {
        view.showLoading();
        Disposable subscription = wksdk.update()
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
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
                .subscribe(new Consumer<RealmOwner>() {
                    @Override
                    public void accept(RealmOwner realmOwner) throws Exception {
                        if (wksdk.getServerConfig().isActive() != Boolean.parseBoolean(realmOwner.getNauta_active())) {
                            wksdk.getServerConfig().setActive(Boolean.parseBoolean(realmOwner.getNauta_active()));
                            WK.getInstance().replaceWKSession(wksdk);
                        }

                        if (wksdk.getServerConfig().isActive())
                            view.updateComplete();
                        else
                            view.showError(new UserInactiveWKException());

                    }
                }, error);
        addSubscription(subscription);
    }

    @Override
    public void onReloads() {
        addSubscription(reloadsInteractor.owner()
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success, error));
    }

    @Override
    public void bindView(@NonNull ReloadsContract.View view) {
        super.bindView(view);
        onReloads();
    }
}
