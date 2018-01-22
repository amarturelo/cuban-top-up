package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.domain.filter.Filter;
import com.wirelesskings.wkreload.model.ReloadItemModel;
import com.wirelesskings.wkreload.presenter.BaseContract;

import java.util.List;

/**
 * Created by alberto on 21/01/18.
 */

public interface SearchContract {
    interface View extends BaseContract.View {
        void renderReloads(List<ReloadItemModel> reloads);

        void hideLoading();

        void showError(Exception e);

        void showLoading();

    }

    interface Presenter extends BaseContract.Presenter<SearchContract.View> {
        void onReloads(List<Filter> filters);
    }
}
