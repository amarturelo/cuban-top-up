package com.wirelesskings.wkreload.model.mapper;

import com.wirelesskings.wkreload.domain.model.Promotion;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.model.PromotionItemModel;
import com.wirelesskings.wkreload.model.ReloadItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 28/10/2017.
 */

public class PromotionItemDataMapper {
    public static List<PromotionItemModel> transform(List<Promotion> promotions) {
        List<PromotionItemModel> promotionItemModels = new ArrayList<>();
        if (promotions != null) {
            for (Promotion reload :
                    promotions) {
                promotionItemModels.add(transform(reload));
            }
        }

        return promotionItemModels;
    }

    public static PromotionItemModel transform(Promotion promotion) {
        PromotionItemModel reloadItem = null;
        if (promotion != null) {
            reloadItem = new PromotionItemModel()
                    .setAmount(promotion.getAmount())
                    .setEndDate(promotion.getEdate())
                    .setTitle(promotion.getPromotion())
                    .setStartDate(promotion.getSdate())
                    .setId(promotion.getId());
        }
        return reloadItem;
    }
}
