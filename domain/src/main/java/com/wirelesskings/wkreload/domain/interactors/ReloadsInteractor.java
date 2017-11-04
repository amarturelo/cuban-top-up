package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.model.CollectionChange;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.ReloadRepository;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

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

    public Single<Reload> reloadById(String id) {
        return reloadRepository.reloadById(id);
    }

    public Observable<Long> debit() {
        return reloadRepository.debit();
    }
}
