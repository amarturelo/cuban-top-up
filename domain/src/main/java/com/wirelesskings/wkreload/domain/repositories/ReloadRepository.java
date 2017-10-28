package com.wirelesskings.wkreload.domain.repositories;

import com.wirelesskings.wkreload.domain.model.CollectionChange;
import com.wirelesskings.wkreload.domain.model.Reload;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Alberto on 28/10/2017.
 */

public interface ReloadRepository {
    Observable<CollectionChange<Reload>> reloads();
}
