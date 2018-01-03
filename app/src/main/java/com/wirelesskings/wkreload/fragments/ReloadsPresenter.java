package com.wirelesskings.wkreload.fragments;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.interactors.OwnerInteractor;
import com.wirelesskings.wkreload.domain.interactors.PromotionInteractor;
import com.wirelesskings.wkreload.domain.interactors.ReloadInteractor;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.domain.model.Promotion;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.model.mapper.ReloadItemDataMapper;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadsPresenter extends BasePresenter<ReloadsContract.View>
        implements ReloadsContract.Presenter {


    private PromotionInteractor promotionInteractor;

    private Consumer<Owner> success = new Consumer<Owner>() {
        @Override
        public void accept(Owner owner) throws Exception {
            view.hideLoading();
            if (owner.getPromotion() != null) {
                view.renderInsertions(ReloadItemDataMapper.transform(owner.getPromotion().getReloads()));
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

    public ReloadsPresenter(PromotionInteractor promotionInteractor, WKSDK wksdk) {
        this.promotionInteractor = promotionInteractor;
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
                .subscribe(new Consumer<WKSDK.WKOwner>() {
                    @Override
                    public void accept(WKSDK.WKOwner wkOwner) throws Exception {
                        if (wksdk.getServerConfig().isActive() != Boolean.parseBoolean(wkOwner.getNauta_active())) {
                            wksdk.getServerConfig().setActive(Boolean.parseBoolean(wkOwner.getNauta_active()));
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
    public void onReloads(String promotionId) {
        addSubscription(promotionInteractor.getPromotionById(promotionId)
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Promotion>() {
                    @Override
                    public void accept(Promotion promotion) throws Exception {
                        view.renderInsertions(ReloadItemDataMapper.transform(promotion.getReloads()));
                    }
                }, error));
    }

    @Override
    public void bindView(@NonNull ReloadsContract.View view) {
        super.bindView(view);
    }
}
