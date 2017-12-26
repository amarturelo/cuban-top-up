package com.wirelesskings.wkreload;

import android.util.Log;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wirelesskings.data.cache.OwnerCache;
import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.ResultListener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

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
                        Log.d("send id", id[0]);
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

    public Single<RealmOwner> reload(List<WKReload> reloads) {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", serverConfig.getCredentials().getUsername());
        params.put("pass", serverConfig.getCredentials().getPassword());
        params.put("user_nauta", serverConfig.getEmail());

        params.put("multiple", Stream.of(reloads)
                .map(new com.annimon.stream.function.Function<WKReload, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(WKReload reload) {
                        Map<String, Object> client = new LinkedHashMap<>();
                        client.put("number", reload.getNumber());
                        client.put("name", reload.getName());
                        client.put("count", reload.getCount());
                        client.put("amount", reload.getAmount());
                        return client;
                    }
                })
                .toList());


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

    public static class WKReload{
        private String number;
        private String name;
        private int count;
        private int amount;

        public WKReload() {
        }

        public String getNumber() {
            return number;
        }

        public WKReload setNumber(String number) {
            this.number = number;
            return this;
        }

        public String getName() {
            return name;
        }

        public WKReload setName(String name) {
            this.name = name;
            return this;
        }

        public int getCount() {
            return count;
        }

        public WKReload setCount(int count) {
            this.count = count;
            return this;
        }

        public int getAmount() {
            return amount;
        }

        public WKReload setAmount(int amount) {
            this.amount = amount;
            return this;
        }
    }
}
