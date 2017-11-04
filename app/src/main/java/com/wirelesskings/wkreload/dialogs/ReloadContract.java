package com.wirelesskings.wkreload.dialogs;

import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.presenter.BaseContract;

/**
 * Created by Alberto on 28/10/2017.
 */

public interface ReloadContract {
    interface View extends BaseContract.View {
        void loading();

        void hideLoading();

        void complete();

        void error(Throwable throwable);
    }

    interface Presenter extends BaseContract.Presenter<ReloadContract.View> {
        void onReload(String wk_user, String wk_pass, String nauta_user, String client_name, String client_number, String reload_count, String reload_amount);
    }
}
