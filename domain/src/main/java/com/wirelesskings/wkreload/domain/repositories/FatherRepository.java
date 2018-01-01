package com.wirelesskings.wkreload.domain.repositories;

import com.wirelesskings.wkreload.domain.model.Father;

import io.reactivex.Observable;

/**
 * Created by alberto on 31/12/17.
 */

public interface FatherRepository {
    Observable<Father> fatherByUser(String wkUser);

    void save(Father father);
}
