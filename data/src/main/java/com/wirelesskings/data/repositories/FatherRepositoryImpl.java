package com.wirelesskings.data.repositories;

import com.wirelesskings.data.cache.FatherCache;
import com.wirelesskings.data.model.RealmFather;
import com.wirelesskings.data.model.mapper.FatherDataMapper;
import com.wirelesskings.wkreload.domain.model.Father;
import com.wirelesskings.wkreload.domain.repositories.FatherRepository;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by alberto on 31/12/17.
 */

public class FatherRepositoryImpl implements FatherRepository {

    private FatherCache fatherCache;

    public FatherRepositoryImpl(FatherCache fatherCache) {
        this.fatherCache = fatherCache;
    }

    @Override
    public void save(Father father) {
        fatherCache.put(new RealmFather()
                .setWkUser(father.getWkUser())
                .setName(father.getName())
                .setCost(father.getCost())
        );
    }

    @Override
    public Observable<Father> fatherByUser(String wkUser) {
        return fatherCache.fatherByUser(wkUser)
                .map(new Function<RealmFather, Father>() {
                    @Override
                    public Father apply(RealmFather realmFather) throws Exception {
                        return FatherDataMapper.transform(realmFather);
                    }
                });
    }
}
