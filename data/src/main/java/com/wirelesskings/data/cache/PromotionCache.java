package com.wirelesskings.data.cache;

import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.wkreload.domain.model.Promotion;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by alberto on 30/12/17.
 */

public interface PromotionCache {
    void put(RealmPromotion realmReload);

    void put(List<RealmPromotion> realmReloads);

    Observable<List<RealmPromotion>> getAll();

    Observable<RealmPromotion> get(String id);
}
