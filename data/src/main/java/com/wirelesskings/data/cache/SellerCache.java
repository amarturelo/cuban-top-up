package com.wirelesskings.data.cache;

import com.wirelesskings.data.model.RealmSeller;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by alberto on 22/01/18.
 */

public interface SellerCache {
    Observable<List<RealmSeller>> getAll(String wkUser);
}
