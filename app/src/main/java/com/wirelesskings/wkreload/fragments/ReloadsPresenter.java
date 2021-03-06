package com.wirelesskings.wkreload.fragments;

import android.support.annotation.NonNull;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.domain.interactors.PromotionInteractor;
import com.wirelesskings.wkreload.domain.model.Promotion;
import com.wirelesskings.wkreload.model.mapper.ReloadItemDataMapper;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadsPresenter extends BasePresenter<ReloadsContract.View>
        implements ReloadsContract.Presenter {


    private PromotionInteractor promotionInteractor;

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
    public void onReloads(String promotionId) {
        addSubscription(promotionInteractor.getPromotionById(promotionId)
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .unsubscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Promotion>() {
                    @Override
                    public void accept(Promotion promotion) throws Exception {
                        view.renderReloads(ReloadItemDataMapper.transform(promotion.getReloads()));
                    }
                }, error));
    }



    @Override
    public void bindView(@NonNull ReloadsContract.View view) {
        super.bindView(view);
    }
}
