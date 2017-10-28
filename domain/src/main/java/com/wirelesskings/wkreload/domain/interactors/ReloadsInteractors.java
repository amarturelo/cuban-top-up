package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.model.CollectionChange;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.ReloadRepository;

import io.reactivex.Observable;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadsInteractors {
    private ReloadRepository reloadRepository;

    public ReloadsInteractors(ReloadRepository reloadRepository) {
        this.reloadRepository = reloadRepository;
    }

    public Observable<CollectionChange<Reload>> reloads() {
        return reloadRepository.reloads();
    }
}
