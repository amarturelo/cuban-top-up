package com.wirelesskings.wkreload.activities;

import com.wirelesskings.wkreload.BackgroundLooper;
import com.wirelesskings.wkreload.domain.filter.Filter;
import com.wirelesskings.wkreload.domain.interactors.ReloadInteractor;
import com.wirelesskings.wkreload.domain.model.Promotion;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.model.mapper.ReloadItemDataMapper;
import com.wirelesskings.wkreload.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by alberto on 21/01/18.
 */

public class SearchPresenter extends BasePresenter<SearchContract.View>
        implements SearchContract.Presenter {

    private ReloadInteractor reloadInteractor;

    public SearchPresenter(ReloadInteractor reloadInteractor) {
        this.reloadInteractor = reloadInteractor;
    }

    @Override
    public void onReloads(ArrayList<Filter> filters) {
        addSubscription(reloadInteractor
                .getByFilters(filters)
                .subscribeOn(AndroidSchedulers.from(BackgroundLooper.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Reload>>() {
                    @Override
                    public void accept(List<Reload> reloads) throws Exception {
                        view.renderReloads(ReloadItemDataMapper.transform(reloads));
                    }
                }, error));
    }

    private Consumer<Throwable> error = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            view.hideLoading();
            view.showError((Exception) throwable);
        }
    };
}
