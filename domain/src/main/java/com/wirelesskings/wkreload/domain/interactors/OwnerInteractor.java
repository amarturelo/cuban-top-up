package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.model.CollectionChange;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.OwnerRepository;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Alberto on 28/10/2017.
 */

public class OwnerInteractor {
    private OwnerRepository ownerRepository;

    public OwnerInteractor(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Observable<CollectionChange<Reload>> reloads() {
        return ownerRepository.reloads();
    }

    public Observable<Owner> owner(){
        return ownerRepository.owner();
    }

    public Single<Reload> reloadById(String id) {
        return ownerRepository.reloadById(id);
    }

    public Observable<Long> debit() {
        return ownerRepository.debit();
    }
}
