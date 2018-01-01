package com.wirelesskings.data.model.mapper;

import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.wkreload.domain.model.Promotion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 02/11/2017.
 */

public class PromotionDataMapper {

    public static List<Promotion> transform(List<RealmPromotion> realmPromotions) {
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

    public static Promotion transform(RealmPromotion realmPromotion) {
        if (realmPromotion != null) {
            Promotion promotion = new Promotion()
                    .setEdate(realmPromotion.getEdate())
                    .setSdate(realmPromotion.getSdate())
                    .setId(realmPromotion.getId())
                    .setPromotion(realmPromotion.getPromotion())
                    .setAmount(realmPromotion.getAmount())
                    .setReloads(ReloadDataMapper.transform(realmPromotion.getRealmReloads()));
            return promotion;
        }
        return null;
    }
}
