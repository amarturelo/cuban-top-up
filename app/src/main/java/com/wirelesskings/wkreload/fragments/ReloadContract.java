package com.wirelesskings.wkreload.fragments;

import com.wirelesskings.wkreload.model.FilterItemModel;
import com.wirelesskings.wkreload.presenter.BaseContract;

import java.util.List;

/**
 * Created by alberto on 23/01/18.
 */

public interface ReloadContract {
    interface View extends BaseContract.View {
        void renderClientNumbers(List<String> clientNumber);

        void hideLoading();

        void showError(Exception e);

        void showLoading();

    }

    interface Presenter extends BaseContract.Presenter<ReloadContract.View> {
        void onClients();
    }
}
