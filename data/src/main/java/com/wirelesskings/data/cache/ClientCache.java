package com.wirelesskings.data.cache;

import com.wirelesskings.data.model.RealmClient;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by alberto on 22/01/18.
 */

public interface ClientCache {
    Observable<List<RealmClient>> getAll(String wkUser);
}
