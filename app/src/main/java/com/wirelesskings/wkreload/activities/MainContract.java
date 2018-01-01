package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.model.FatherModel;
import com.wirelesskings.wkreload.model.PromotionItemModel;
import com.wirelesskings.wkreload.presenter.BaseContract;

import java.util.List;

/**
 * Created by alberto on 31/12/17.
 */

public interface MainContract {
    interface View extends BaseContract.View {

        void showLoading();

        void hideLoading();

        void showError(Exception e);

        void renderPromotionList(List<PromotionItemModel> promotionItemModels);

        void renderPromotion(PromotionItemModel promotionItemModel);

        void renderFather(FatherModel fatherModel);
    }

    interface Presenter extends BaseContract.Presenter<MainContract.View> {

        void getAllPromotions(String wkUser);

        void getPromotionById(String id);

        void getFatherByUser(String wkUser);

    }
}
