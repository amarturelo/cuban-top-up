package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.filter.Filter;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.ReloadRepository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by alberto on 31/12/17.
 */

public class ReloadInteractor {
    private ReloadRepository reloadRepository;

    public ReloadInteractor(ReloadRepository reloadRepository) {
        this.reloadRepository = reloadRepository;
    }

    public Observable<List<Reload>> getAll() {
        return reloadRepository.reloads();
    }

    public Flowable<List<Reload>> getByFilters(List<Filter> filters) {
        return reloadRepository.reloadsByFilters(filters);
    }

    public Single<Reload> reloadById(String id) {
        return reloadRepository.reloadById(id);
    }
}
