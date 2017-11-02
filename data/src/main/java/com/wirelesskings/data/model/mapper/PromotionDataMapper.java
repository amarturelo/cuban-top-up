package com.wirelesskings.data.model.mapper;

import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.wkreload.domain.model.Promotion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 02/11/2017.
 */

public class PromotionDataMapper implements DataMapper<RealmPromotion, Promotion> {

    private ReloadDataMapper reloadDataMapper;

    public PromotionDataMapper(ReloadDataMapper reloadDataMapper) {
        this.reloadDataMapper = reloadDataMapper;
    }

    @Override
    public List<Promotion> transform(List<RealmPromotion> realmPromotions) {
        List<Promotion> promotions = new ArrayList<>();
        for (RealmPromotion realmPromotion :
                realmPromotions) {
            if (realmPromotions != null) {
                Promotion promotion = transform(realmPromotion);
                if (promotion != null)
                    promotions.add(promotion);
            }
        }
        return promotions;
    }

    @Override
    public Promotion transform(RealmPromotion realmPromotion) {
        if (realmPromotion != null) {
            Promotion promotion = new Promotion()
                    .setEdate(realmPromotion.getEdate())
                    .setSdate(realmPromotion.getSdate())
                    .setPromotion(realmPromotion.getPromotion())
                    .setReloads(reloadDataMapper.transform(realmPromotion.getRealmReloads()));
            return promotion;
        }
        return null;
    }
}
