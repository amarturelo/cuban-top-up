package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.model.CollectionChange;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.ReloadRepository;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadsInteractor {
    private ReloadRepository reloadRepository;

    public ReloadsInteractor(ReloadRepository reloadRepository) {
        this.reloadRepository = reloadRepository;
    }

    public Observable<CollectionChange<Reload>> reloads() {
        return reloadRepository.reloads();
    }

    public Completable reload(String client_name, String client_number, int count, int amount) {
        return reloadRepository.reload(client_name,client_number, count,amount);
    }
}
