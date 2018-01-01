package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.domain.interactors.FatherInteractor;
import com.wirelesskings.wkreload.domain.interactors.PromotionInteractor;
import com.wirelesskings.wkreload.domain.model.Father;
import com.wirelesskings.wkreload.domain.model.Promotion;
import com.wirelesskings.wkreload.model.FatherModel;
import com.wirelesskings.wkreload.model.mapper.PromotionItemDataMapper;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by alberto on 31/12/17.
 */

public class MainPresenter extends BasePresenter<MainContract.View>
        implements MainContract.Presenter {

    private PromotionInteractor promotionInteractor;

    private FatherInteractor fatherInteractor;

    public MainPresenter(PromotionInteractor promotionInteractor, FatherInteractor fatherInteractor) {
        this.promotionInteractor = promotionInteractor;
        this.fatherInteractor = fatherInteractor;
    }

    @Override
    public void getAllPromotions(String wkUser) {
        addSubscription(promotionInteractor.getAllPromotions(wkUser)
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Promotion>>() {
                               @Override
                               public void accept(List<Promotion> promotions) throws Exception {
                                   view.renderPromotionList(PromotionItemDataMapper.transform(promotions));
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
    public void getPromotionById(String id) {
        addSubscription(promotionInteractor.getPromotionById(id)
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Promotion>() {
                    @Override
                    public void accept(Promotion promotion) throws Exception {
                        view.renderPromotion(PromotionItemDataMapper.transform(promotion));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showError((Exception) throwable);
                    }
                }));
    }

    @Override
    public void getFatherByUser(String wkUser) {
        addSubscription(fatherInteractor.getFatherByUser(wkUser)
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Father>() {
                               @Override
                               public void accept(Father father) throws Exception {
                                   view.renderFather(new FatherModel()
                                           .setName(father.getName())
                                           .setCount(father.getCost()));
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.showError((Exception) throwable);
                            }
                        })
        );
    }
}
