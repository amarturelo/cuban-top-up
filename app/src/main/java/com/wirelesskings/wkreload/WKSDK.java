package com.wirelesskings.wkreload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wirelesskings.data.cache.OwnerCache;
import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.ResultListener;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by alberto on 5/12/17.
 */

public class WKSDK {
    private Middleware middleware;
    private ServerConfig serverConfig;
    private Gson gson;

    private OwnerCache mOwnerCache;

    public WKSDK(ServerConfig serverConfig, OwnerCache ownerCache) {
        this.mOwnerCache = ownerCache;
        this.serverConfig = serverConfig;
        this.gson = new Gson();

        middleware = new Middleware(serverConfig.getEmail()
                , serverConfig.getPassword()
                , serverConfig.getCredentials().getToken());
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public Single<RealmOwner> update() {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", serverConfig.getCredentials().getUsername());
        params.put("pass", serverConfig.getCredentials().getPassword());
        params.put("user_nauta", serverConfig.getEmail());

        final String[] id = new String[1];
        return Single
                .create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(final SingleEmitter<String> emitter) throws Exception {
                        id[0] = middleware.call("update", params, new ResultListener() {
                            @Override
                            public void onSuccess(String result) {
                                emitter.onSuccess(result);
                            }

                            @Override
                            public void onError(Exception e) {
                                emitter.onError(e);
                            }
                        });
                    }
                })
                .map(new Function<String, RealmOwner>() {
                    @Override
                    public RealmOwner apply(String s) throws Exception {
                        JsonParser parser = new JsonParser();
                        JsonElement mJson = parser.parse(s.trim());
                        return gson.fromJson(mJson, RealmOwner.class);
                    }
                })
                .doOnSuccess(new Consumer<RealmOwner>() {
                    @Override
                    public void accept(RealmOwner realmOwner) throws Exception {
                        mOwnerCache.put(realmOwner);
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        middleware.cancel(id[0]);
                    }
                })
                ;
    }

    public Single<RealmOwner> reload(String client_name, String client_number, String reload_count, String reload_amount) {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", serverConfig.getCredentials().getUsername());
        params.put("pass", serverConfig.getCredentials().getPassword());
        params.put("user_nauta", serverConfig.getEmail());

        Map<String, Object> client = new LinkedHashMap<>();
        client.put("number", client_number);
        client.put("name", client_name);

        params.put("client", client);

        Map<String, Object> reload = new LinkedHashMap<>();
        reload.put("count", reload_count);
        reload.put("amount", reload_amount);

        params.put("reload", reload);


        final String[] id = new String[1];

        return Single
                .create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull final SingleEmitter<String> emitter) throws Exception {
                        id[0] = middleware.call("reload", params, new ResultListener() {
                            @Override
                            public void onSuccess(String result) {
                                emitter.onSuccess(result);
                            }

                            @Override
                            public void onError(Exception e) {
                                emitter.onError(e);
                            }
                        });
                    }
                })
                .map(new Function<String, RealmOwner>() {
                    @Override
                    public RealmOwner apply(@NonNull String s) throws Exception {
                        JsonParser parser = new JsonParser();
                        JsonElement mJson = parser.parse(s.trim());
                        return gson.fromJson(mJson, RealmOwner.class);
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        middleware.cancel(id[0]);
                    }
                });
    }
}
