package com.wirelesskings.wkreload.fragments;

import com.wirelesskings.wkreload.model.ReloadItemModel;
import com.wirelesskings.wkreload.presenter.BaseContract;

import java.util.List;

/**
 * Created by Alberto on 28/10/2017.
 */

public interface ReloadsContract {
    interface View extends BaseContract.View {
        void renderReloads(List<ReloadItemModel> reloads);

        void hideLoading();

        void showError(Exception e);

        void showLoading();

    }

    interface Presenter extends BaseContract.Presenter<ReloadsContract.View> {
        void onReloads(String promotionId);
        void cancel();
    }
}
