package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.model.PreReloadItemModel;
import com.wirelesskings.wkreload.presenter.BaseContract;

import java.util.List;

/**
 * Created by alberto on 1/01/18.
 */

public interface ReloadContract {
    interface View extends BaseContract.View {

        void showLoading();

        void hideLoading();

        void showError(Exception e);

        void reloadComplete();
    }

    interface Presenter extends BaseContract.Presenter<ReloadContract.View> {

        void reload(List<PreReloadItemModel> wkUser);

    }
}
