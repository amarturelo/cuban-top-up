package com.wirelesskings.wkreload.domain.repositories;

import com.wirelesskings.wkreload.domain.model.CollectionChange;
import com.wirelesskings.wkreload.domain.model.Reload;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Alberto on 28/10/2017.
 */

public interface ReloadRepository {
    Observable<CollectionChange<Reload>> reloads();

    Completable reload(String client_name, String client_number, int count, int amount);

    Single<Reload> reloadById(String id);

    Observable<Long> debit();
}
