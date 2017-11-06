package com.wirelesskings.data.model.mapper;

import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.wkreload.domain.model.Father;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.domain.model.Promotion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 02/11/2017.
 */

public class OwnerDataMapper implements DataMapper<RealmOwner, Owner> {

    private FatherDataMapper fatherDataMapper;
    private PromotionDataMapper promotionDataMapper;

    public OwnerDataMapper(FatherDataMapper fatherDataMapper, PromotionDataMapper promotionDataMapper) {
        this.fatherDataMapper = fatherDataMapper;
        this.promotionDataMapper = promotionDataMapper;
    }

    @Override
    public List<Owner> transform(List<RealmOwner> realmOwners) {
        List<Owner> owners = new ArrayList<>();
        if (realmOwners != null)
            for (RealmOwner owner :
                    realmOwners) {
                Owner o = transform(owner);
                if (o != null)
                    owners.add(o);
            }
        return owners;
    }

    @Override
    public Owner transform(RealmOwner realmOwner) {
        if (realmOwner != null) {
            Owner owner = new Owner()
                    .setNauta_active(realmOwner.getNauta_active())
                    .setUser_nauta(realmOwner.getUser_nauta())
                    .setPromotion(promotionDataMapper.transform(realmOwner.getPromotion()))
                    .setFather(fatherDataMapper.transform(realmOwner.getFather()));
            return owner;
        }
        return null;
    }
}
