package com.wirelesskings.wkreload.domain.repositories;

import com.wirelesskings.wkreload.domain.filter.Filter;
import com.wirelesskings.wkreload.domain.model.Reload;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by alberto on 30/12/17.
 */

public interface ReloadRepository {
    Observable<List<Reload>> reloads();

    Single<Reload> reloadById(String id);

    Flowable<List<Reload>> reloadsByFilters(List<Filter> filters);
}
