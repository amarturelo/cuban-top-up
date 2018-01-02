package com.wirelesskings.wkreload;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.wirelesskings.data.cache.FatherCache;
import com.wirelesskings.data.cache.PromotionCache;
import com.wirelesskings.data.model.RealmClient;
import com.wirelesskings.data.model.RealmFather;
import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.data.model.RealmSeller;

import io.realm.RealmList;

/**
 * Created by alberto on 1/01/18.
 */

public class CacheHelper {

    private FatherCache fatherCache;
    private PromotionCache promotionCache;

    public CacheHelper(FatherCache fatherCache, PromotionCache promotionCache) {
        this.fatherCache = fatherCache;
        this.promotionCache = promotionCache;
    }

    public void save(WKSDK.WKOwner wkOwner, String wkUser) {
        fatherCache.put(new RealmFather()
                .setWkUser(wkUser)
                .setCost(wkOwner.getFather().getCost())
                .setName(wkOwner.getFather().getName()));

        if (wkOwner.getPromotion() != null) {
            final RealmList<RealmReload> realmReloads = new RealmList<>();

            Stream.of(wkOwner.getPromotion().getReloads())
                    .map(new Function<WKSDK.WKReload, RealmReload>() {
                        @Override
                        public RealmReload apply(WKSDK.WKReload wkReload) {
                            return new RealmReload()
                                    .setId(wkReload.getId())
                                    .setAmount(wkReload.getAmount())
                                    .setApp(wkReload.getApp())
                                    .setClient(new RealmClient()
                                            .setName(wkReload.getClient().getName())
                                            .setNumber(wkReload.getClient().getNumber()))
                                    .setSeller(new RealmSeller()
                                            .setName(wkReload.getSeller().getName())
                                            .setAmount(wkReload.getSeller().getAmount()))
                                    .setCount(wkReload.getCount())
                                    .setStatus(wkReload.getStatus());
                        }
                    }).forEach(new Consumer<RealmReload>() {
                @Override
                public void accept(RealmReload realmReload) {
                    realmReloads.add(realmReload);
                }
            });

            promotionCache.put(new RealmPromotion()
                    .setPromotion(wkOwner.getPromotion().getTitle())
                    .setId(wkOwner.getPromotion().getId())
                    .setAmount(wkOwner.getFather().getAmount())
                    .setSdate(wkOwner.getPromotion().getSdate())
                    .setEdate(wkOwner.getPromotion().getEdate())
                    .setReloads(realmReloads)
            );
        }
    }
}
