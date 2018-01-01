package com.wirelesskings.wkreload.domain.repositories;

import com.annimon.stream.Stream;
import com.wirelesskings.wkreload.domain.model.Promotion;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by alberto on 30/12/17.
 */

public interface PromotionRepository {
    Observable<List<Promotion>> promotions(String wkUser);

    Single<Promotion> promotionById(String id);
}
