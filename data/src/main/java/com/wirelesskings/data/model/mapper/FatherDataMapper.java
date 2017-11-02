package com.wirelesskings.data.model.mapper;

import com.wirelesskings.data.model.RealmFather;
import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.wkreload.domain.model.Father;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 01/11/2017.
 */

public class FatherDataMapper implements DataMapper<RealmFather, Father> {
    @Override
    public List<Father> transform(List<RealmFather> realmFathers) {
        List<Father> fathers = new ArrayList<>();
        if (realmFathers != null) {
            for (RealmFather realmFather :
                    realmFathers) {
                Father father = transform(realmFather);
                if (father != null)
                    fathers.add(father);
            }
        }
        return fathers;
    }

    @Override
    public Father transform(RealmFather realmFather) {
        if (realmFather != null) {
            Father father = new Father()
                    .setName(realmFather.getName())
                    .setAmount(realmFather.getAmount());
            return father;
        }
        return null;
    }
}
