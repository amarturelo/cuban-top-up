package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.CacheHelper;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.executor.ThreadExecutor;
import com.wirelesskings.wkreload.domain.interactors.FatherInteractor;
import com.wirelesskings.wkreload.domain.interactors.PromotionInteractor;
import com.wirelesskings.wkreload.domain.model.Father;
import com.wirelesskings.wkreload.domain.model.Promotion;
import com.wirelesskings.wkreload.model.FatherModel;
import com.wirelesskings.wkreload.model.mapper.PromotionItemDataMapper;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by alberto on 31/12/17.
 */

public class MainPresenter extends BasePresenter<MainContract.View>
        implements MainContract.Presenter {
    private ThreadExecutor threadExecutor;

    private PromotionInteractor promotionInteractor;

    private FatherInteractor fatherInteractor;

    private WKSDK wksdk;
    private CacheHelper cacheHelper;

    public MainPresenter(ThreadExecutor threadExecutor, PromotionInteractor promotionInteractor, FatherInteractor fatherInteractor, WKSDK wksdk, CacheHelper cacheHelper) {
        this.threadExecutor = threadExecutor;
        this.promotionInteractor = promotionInteractor;
        this.fatherInteractor = fatherInteractor;
        this.wksdk = wksdk;
        this.cacheHelper = cacheHelper;
    }

    @Override
    public void getAllPromotions() {
        addSubscription(promotionInteractor.getAllPromotions()
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .unsubscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
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
                .unsubscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
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
    public void getFatherByUser() {
        addSubscription(fatherInteractor.getFatherByUser()
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .unsubscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
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

    @Override
    public void update() {
        addSubscription(wksdk.update()
                .doOnSuccess(new Consumer<WKSDK.WKOwner>() {
                    @Override
                    public void accept(WKSDK.WKOwner wkOwner) throws Exception {
                        cacheHelper.save(wkOwner, wksdk
                                .getServerConfig()
                                .getCredentials()
                                .getUsername());
                    }
                })
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        view.showLoading(disposable);
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
                               public void accept(WKSDK.WKOwner realmOwner) throws Exception {
                                   wksdk.getServerConfig().setActive(realmOwner.getNauta_active().equals("1"));
                                   requestDone(wksdk);

                                   if (!realmOwner.getNauta_active().equals("1"))
                                       view.showError(new UserInactiveWKException());
                                   else
                                       view.updateComplete();

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                view.showError((Exception) throwable);
                            }
                        })
        );
    }

    private void requestDone(WKSDK wksdk) {
        WK.getInstance().replaceWKSession(wksdk);
    }
}
