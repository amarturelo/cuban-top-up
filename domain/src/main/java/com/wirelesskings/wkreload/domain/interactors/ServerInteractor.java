package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.model.Owner;
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



    public Single<Owner> update(String wk_user, String wk_pass, String nauta_user) {
        return serverRepository.update(nauta_user, wk_user, wk_pass);
    }

    public Single<Owner> reload(String wk_user, String wk_pass, String nauta_user, String client_name, String client_number, String reload_count, String reload_amount){
        return serverRepository.reload(wk_user, wk_pass, nauta_user, client_name, client_number, reload_count, reload_amount);
    }
}
