package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.model.Father;
import com.wirelesskings.wkreload.domain.repositories.FatherRepository;

import io.reactivex.Observable;

/**
 * Created by alberto on 31/12/17.
 */

public class FatherInteractor {
    private FatherRepository fatherRepository;

    public FatherInteractor(FatherRepository fatherRepository) {
        this.fatherRepository = fatherRepository;
    }

    public Observable<Father> getFatherByUser() {
        return fatherRepository.fatherByUser();
    }

    public void save(Father father) {
        fatherRepository.save(father);
    }
}
