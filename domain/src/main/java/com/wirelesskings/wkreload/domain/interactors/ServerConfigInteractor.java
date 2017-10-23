package com.wirelesskings.wkreload.domain.interactors;

import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.domain.repositories.ServerConfigRepositoy;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ServerConfigInteractor {

    private ServerConfigRepositoy repositoy;

    public ServerConfigInteractor(ServerConfigRepositoy repositoy) {
        this.repositoy = repositoy;
    }

    public Single<ServerConfig> getServerConfig() {
        return repositoy.get();
    }

    public Completable saveServerConfig(String email, String password){
        return repositoy.save(email, password);
    }
}
