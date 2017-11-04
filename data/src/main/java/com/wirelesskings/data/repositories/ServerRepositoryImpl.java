package com.wirelesskings.data.repositories;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wirelesskings.data.cache.OwnerCache;
import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.data.model.mapper.OwerDataMapper;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.domain.repositories.ServerRepository;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.ResultListener;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Alberto on 24/10/2017.
 */

public class ServerRepositoryImpl implements ServerRepository {


    private Middleware middleware;

    private Gson gson;

    private OwerDataMapper owerDataMapper;

    private OwnerCache ownerCache;

    public ServerRepositoryImpl(Middleware middleware, OwerDataMapper owerDataMapper, OwnerCache ownerCache) {
        this.middleware = middleware;
        this.owerDataMapper = owerDataMapper;
        this.ownerCache = ownerCache;
        this.gson = new Gson();
    }

    @Override
    public Single<Owner> update(String nauta_mail, String wk_username, String wk_password, String token) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", wk_username);
        params.put("pass", wk_password);
        params.put("user_nauta", nauta_mail);

        final String[] id = new String[1];
        return Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<String> e) throws Exception {
                id[0] = middleware.call("update", token, params, new ResultListener() {
                    @Override
                    public void onSuccess(String result) {
                        e.onSuccess(result);
                    }

                    @Override
                    public void onError(String error, String reason, String details) {
                        e.onError(new Exception(error));
                    }
                });
            }
        }).map(s -> {
            JsonParser parser = new JsonParser();
            JsonElement mJson = parser.parse(s.trim());
            return gson.fromJson(mJson, RealmOwner.class);
        })
                .doOnSuccess(realmOwner -> ownerCache.put(realmOwner))
                .map(realmOwner -> owerDataMapper.transform(realmOwner))
                .doOnDispose(() -> middleware.cancel(id[0]))
                ;
    }
}
