package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.model.FatherModel;
import com.wirelesskings.wkreload.model.PromotionItemModel;
import com.wirelesskings.wkreload.presenter.BaseContract;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by alberto on 31/12/17.
 */

public interface MainContract {
    interface View extends BaseContract.View {

        void showLoading(Disposable disposable);

        void hideLoading();

        void showError(Exception e);

        void renderPromotionList(List<PromotionItemModel> promotionItemModels);

        void renderPromotion(PromotionItemModel promotionItemModel);

        void renderFather(FatherModel fatherModel);

        void updateComplete();
    }

    interface Presenter extends BaseContract.Presenter<MainContract.View> {

        void getAllPromotions();

        void getPromotionById(String id);

        void getFatherByUser();

        void update();
    }
}
