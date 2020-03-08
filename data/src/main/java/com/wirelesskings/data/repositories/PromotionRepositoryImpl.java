package com.wirelesskings.data.repositories;

import com.wirelesskings.data.cache.PromotionCache;
import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.data.model.mapper.PromotionDataMapper;
import com.wirelesskings.wkreload.domain.model.Promotion;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.PromotionRepository;
import com.wirelesskings.wkreload.domain.repositories.ReloadRepository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by alberto on 31/12/17.
 */

public class PromotionRepositoryImpl implements PromotionRepository {

    private PromotionCache promotionCache;

    public PromotionRepositoryImpl(PromotionCache promotionCache) {
        this.promotionCache = promotionCache;
    }

    @Override
    public Observable<List<Promotion>> promotions() {
        return promotionCache.getAll()
                .map(new Function<List<RealmPromotion>, List<Promotion>>() {
                    @Override
                    public List<Promotion> apply(List<RealmPromotion> realmPromotions) throws Exception {
                        return PromotionDataMapper.transform(realmPromotions);
                    }
                });
    }

    @Override
    public Observable<Promotion> promotionById(String id) {
        return promotionCache.get(id)
                .map(new Function<RealmPromotion, Promotion>() {
                    @Override
                    public Promotion apply(RealmPromotion realmPromotion) throws Exception {
                        return PromotionDataMapper.transform(realmPromotion);
                    }
                });
    }
}
