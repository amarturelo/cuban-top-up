package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.model.Promotion;
import com.wirelesskings.wkreload.domain.repositories.PromotionRepository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by alberto on 31/12/17.
 */

public class PromotionInteractor {

    private PromotionRepository promotionRepository;

    public PromotionInteractor(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public Observable<List<Promotion>> getAllPromotions(String wkUser) {
        return promotionRepository.promotions(wkUser);
    }

    public Observable<Promotion> getPromotionById(String id) {
        return promotionRepository.promotionById(id);
    }

    public void save(Promotion promotion) {

    }
}
