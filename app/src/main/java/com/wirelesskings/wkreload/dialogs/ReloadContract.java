package com.wirelesskings.wkreload.dialogs;

import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.presenter.BaseContract;

/**
 * Created by Alberto on 28/10/2017.
 */

public interface ReloadContract {
    interface View extends BaseContract.View {
        void loading();

        void complete();

        void error(Throwable throwable);
    }

    interface Presenter extends BaseContract.Presenter<ReloadContract.View> {
        void onReload(String clientName, String clintNumber, int amount, int count);
    }
}
