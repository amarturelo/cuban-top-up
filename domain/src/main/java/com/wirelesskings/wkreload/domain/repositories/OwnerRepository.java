package com.wirelesskings.wkreload.domain.repositories;

import com.wirelesskings.wkreload.domain.model.CollectionChange;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.domain.model.Reload;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Alberto on 28/10/2017.
 */

public interface OwnerRepository {
    Observable<CollectionChange<Reload>> reloads();

    Single<Reload> reloadById(String id);

    Observable<Long> debit();

    Observable<Owner> owner();
}
