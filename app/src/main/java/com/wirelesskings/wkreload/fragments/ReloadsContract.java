package com.wirelesskings.wkreload.fragments;

import com.wirelesskings.wkreload.model.ReloadItem;
import com.wirelesskings.wkreload.presenter.BaseContract;

import java.util.List;

/**
 * Created by Alberto on 28/10/2017.
 */

public interface ReloadsContract {
    interface View extends BaseContract.View {
        void renderInsertions(List<ReloadItem> reloads);
        void renderDeletions(List<ReloadItem> reloads);
        void renderChanges(List<ReloadItem> reloads);

        void renderDebit(long debit);

        void hideLoading();

        void updateComplete();

        void showError(Exception e);

        void showLoading();
    }

    interface Presenter extends BaseContract.Presenter<ReloadsContract.View> {
        void onReloads();

        void onDebit();

        void update();

        void cancel();
    }
}
