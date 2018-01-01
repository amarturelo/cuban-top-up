package com.wirelesskings.wkreload;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wirelesskings.data.model.RealmClient;
import com.wirelesskings.data.model.RealmSeller;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.ResultListener;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by alberto on 5/12/17.
 */

public class WKSDK {
    private Middleware middleware;
    private ServerConfig serverConfig;
    private Gson gson;

    public WKSDK(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        this.gson = new Gson();

        middleware = new Middleware(serverConfig.getEmail()
                , serverConfig.getPassword()
                , serverConfig.getCredentials().getToken());
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public Single<WKOwner> update() {
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
                .map(new Function<String, WKOwner>() {
                    @Override
                    public WKOwner apply(String s) throws Exception {
                        JsonParser parser = new JsonParser();
                        JsonElement mJson = parser.parse(s.trim());
                        return gson.fromJson(mJson, WKOwner.class);
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        middleware.cancel(id[0]);
                    }
                });
    }

    public Single<WKOwner> reload(List<ReloadParams> reloads) {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", serverConfig.getCredentials().getUsername());
        params.put("pass", serverConfig.getCredentials().getPassword());
        params.put("user_nauta", serverConfig.getEmail());

        params.put("reload", Stream.of(reloads)
                .map(new com.annimon.stream.function.Function<ReloadParams, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(ReloadParams reload) {
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
                        id[0] = middleware.call("multiple", params, new ResultListener() {
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
                .map(new Function<String, WKOwner>() {
                    @Override
                    public WKOwner apply(@NonNull String s) throws Exception {
                        JsonParser parser = new JsonParser();
                        JsonElement mJson = parser.parse(s.trim());
                        return gson.fromJson(mJson, WKOwner.class);
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        middleware.cancel(id[0]);
                    }
                });
    }

    public static class WKOwner {
        private String user_nauta;
        private String nauta_active;
        private WKPromotion promotion;
        private WKFather father;

        public WKOwner() {
        }

        public String getUser_nauta() {
            return user_nauta;
        }

        public WKOwner setUser_nauta(String user_nauta) {
            this.user_nauta = user_nauta;
            return this;
        }

        public String getNauta_active() {
            return nauta_active;
        }

        public WKOwner setNauta_active(String nauta_active) {
            this.nauta_active = nauta_active;
            return this;
        }

        public WKPromotion getPromotion() {
            return promotion;
        }

        public WKOwner setPromotion(WKPromotion promotion) {
            this.promotion = promotion;
            return this;
        }

        public WKFather getFather() {
            return father;
        }

        public WKOwner setFather(WKFather father) {
            this.father = father;
            return this;
        }
    }

    public static class WKFather {
        private String name;
        private String cost;
        private String amount;

        public WKFather() {
        }

        public String getName() {
            return name;
        }

        public WKFather setName(String name) {
            this.name = name;
            return this;
        }

        public String getCost() {
            return cost;
        }

        public WKFather setCost(String cost) {
            this.cost = cost;
            return this;
        }

        public String getAmount() {
            return amount;
        }

        public WKFather setAmount(String amount) {
            this.amount = amount;
            return this;
        }
    }

    public static class WKPromotion {
        private String title;

        private String id;

        private String amount;

        private Date sdate;

        private Date edate;
        private List<WKReload> reloads;

        public WKPromotion() {
        }

        public String getTitle() {
            return title;
        }

        public WKPromotion setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getId() {
            return id;
        }

        public WKPromotion setId(String id) {
            this.id = id;
            return this;
        }

        public String getAmount() {
            return amount;
        }

        public WKPromotion setAmount(String amount) {
            this.amount = amount;
            return this;
        }

        public Date getSdate() {
            return sdate;
        }

        public WKPromotion setSdate(Date sdate) {
            this.sdate = sdate;
            return this;
        }

        public Date getEdate() {
            return edate;
        }

        public WKPromotion setEdate(Date edate) {
            this.edate = edate;
            return this;
        }

        public List<WKReload> getReloads() {
            return reloads;
        }

        public WKPromotion setReloads(List<WKReload> reloads) {
            this.reloads = reloads;
            return this;
        }
    }

    public static class WKReload {
        private String id;
        private RealmClient client;
        private RealmSeller seller;
        private int count;
        private int amount;
        private String date;
        private String status;
        private String app;

        public WKReload() {
        }

        public String getId() {
            return id;
        }

        public WKReload setId(String id) {
            this.id = id;
            return this;
        }

        public RealmClient getClient() {
            return client;
        }

        public WKReload setClient(RealmClient client) {
            this.client = client;
            return this;
        }

        public RealmSeller getSeller() {
            return seller;
        }

        public WKReload setSeller(RealmSeller seller) {
            this.seller = seller;
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

        public String getDate() {
            return date;
        }

        public WKReload setDate(String date) {
            this.date = date;
            return this;
        }

        public String getStatus() {
            return status;
        }

        public WKReload setStatus(String status) {
            this.status = status;
            return this;
        }

        public String getApp() {
            return app;
        }

        public WKReload setApp(String app) {
            this.app = app;
            return this;
        }
    }

    public static class ReloadParams {
        private String number;
        private String name;
        private int count;
        private int amount;

        public ReloadParams() {
        }

        public String getNumber() {
            return number;
        }

        public ReloadParams setNumber(String number) {
            this.number = number;
            return this;
        }

        public String getName() {
            return name;
        }

        public ReloadParams setName(String name) {
            this.name = name;
            return this;
        }

        public int getCount() {
            return count;
        }

        public ReloadParams setCount(int count) {
            this.count = count;
            return this;
        }

        public int getAmount() {
            return amount;
        }

        public ReloadParams setAmount(int amount) {
            this.amount = amount;
            return this;
        }
    }
}
