package com.wirelesskings.wkreload.domain.repositories;

import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Alberto on 18/10/2017.
 */

public interface ServerConfigRepositoy {
    Single<ServerConfig> get();

    Completable save(String username, String password);
}
