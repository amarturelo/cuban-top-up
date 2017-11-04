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
import io.reactivex.functions.Function;

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
    public Single<Owner> reload(String wk_user, String wk_pass, String nauta_user, String client_name, String client_number, String reload_count, String reload_amount) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", wk_user);
        params.put("pass", wk_pass);
        params.put("user_nauta", nauta_user);

        Map<String, Object> client = new LinkedHashMap<>();
        client.put("name", client_name);
        client.put("number", client_number);

        params.put("client", client);

        Map<String, Object> reload = new LinkedHashMap<>();
        client.put("count", reload_count);
        client.put("amount", reload_amount);

        params.put("reload", reload);

        final String[] id = new String[1];
        return Single.create((SingleOnSubscribe<String>) emitter -> id[0] = middleware.call("reload", params, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                emitter.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                emitter.onError(e);
            }
        })).map(s -> {
            JsonParser parser = new JsonParser();
            JsonElement mJson = parser.parse(s.trim());
            return gson.fromJson(mJson, RealmOwner.class);
        })
                .doOnSuccess(realmOwner -> ownerCache.put(realmOwner))
                .map(realmOwner -> owerDataMapper.transform(realmOwner))
                .doOnDispose(() -> middleware.cancel(id[0]));
    }

    @Override
    public Single<Owner> update(String nauta_mail, String wk_username, String wk_password) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", wk_username);
        params.put("pass", wk_password);
        params.put("user_nauta", nauta_mail);

        final String[] id = new String[1];
        return Single.create((SingleOnSubscribe<String>) emitter -> id[0] = middleware.call("update", params, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                emitter.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                emitter.onError(e);
            }
        })).map(s -> {
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
