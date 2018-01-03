package com.wirelesskings.wkreload.fragments;

import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.presenter.BaseContract;

/**
 * Created by Alberto on 28/10/2017.
 */

public interface ViewReloadContract {

    interface View extends BaseContract.View {
        void showLoading();
void hideLoading();
        void renderReload(Reload reload);
    }

    interface Presenter extends BaseContract.Presenter<ViewReloadContract.View> {
        void onViewReload(String id);
    }
}
