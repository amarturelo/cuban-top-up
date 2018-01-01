package com.wirelesskings.data.cache;

import com.wirelesskings.data.model.RealmFather;

import io.reactivex.Observable;

/**
 * Created by alberto on 31/12/17.
 */

public interface FatherCache {
    Observable<RealmFather> fatherByUser(String wkUser);

    void put(RealmFather realmFather);
}
