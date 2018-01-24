package com.wirelesskings.wkreload.domain.repositories;

import com.wirelesskings.wkreload.domain.model.Promotion;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by alberto on 30/12/17.
 */

public interface PromotionRepository {
    Observable<List<Promotion>> promotions();

    Observable<Promotion> promotionById(String id);
}
