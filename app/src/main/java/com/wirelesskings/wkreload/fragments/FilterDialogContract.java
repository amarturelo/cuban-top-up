package com.wirelesskings.wkreload.fragments;

import com.wirelesskings.wkreload.model.FilterItemModel;
import com.wirelesskings.wkreload.presenter.BaseContract;

import java.util.List;

/**
 * Created by alberto on 21/01/18.
 */

public interface FilterDialogContract {
    interface View extends BaseContract.View {
        void renderClientName(List<FilterItemModel> clientName);

        void renderClientNumbers(List<FilterItemModel> clientNumber);

        void renderSellerName(List<FilterItemModel> clientNumber);

        void hideLoading();

        void showError(Exception e);

        void showLoading();

    }

    interface Presenter extends BaseContract.Presenter<FilterDialogContract.View> {
        void onClients(String ownerId);


        void onSellerName(String ownerId);
    }
}
