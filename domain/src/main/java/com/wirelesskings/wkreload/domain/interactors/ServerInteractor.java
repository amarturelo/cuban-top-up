package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.repositories.ServerRepository;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Alberto on 24/10/2017.
 */

public class ServerInteractor {

    private ServerRepository serverRepository;

    public ServerInteractor(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public Completable update(String wk_user, String wk_pass, String nauta_user) {
        return serverRepository.update(nauta_user, wk_user, wk_pass);
    }
}
