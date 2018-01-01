package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.presenter.BaseContract;

/**
 * Created by alberto on 1/01/18.
 */

public interface ReloadContract {
    interface View extends BaseContract.View {

        void showLoading();

        void hideLoading();

        void showError(Exception e);


    }

    interface Presenter extends BaseContract.Presenter<ReloadContract.View> {

        void reload(String wkUser);

    }
}
