package com.wirelesskings.data.repositories;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wirelesskings.data.model.RealmFather;
import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.data.model.mapper.OwerDataMapper;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.domain.repositories.ServerRepository;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.ResultListener;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;

/**
 * Created by Alberto on 24/10/2017.
 */

public class ServerRepositoryImpl implements ServerRepository {


    private Middleware middleware;

    private Gson gson;

    private OwerDataMapper owerDataMapper;

    public ServerRepositoryImpl(Middleware middleware, OwerDataMapper owerDataMapper) {
        this.middleware = middleware;
        this.owerDataMapper = owerDataMapper;
        this.gson = new Gson();
    }

    @Override
    public Single<Owner> update(String nauta_mail, String wk_username, String wk_password) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", wk_username);
        params.put("pass", wk_password);
        params.put("user_nauta", nauta_mail);

        final String[] id = new String[1];
        return /*Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<String> e) throws Exception {
                id[0] = middleware.call("update", params, new ResultListener() {
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
        })*/Single.just("{\n" +
                "    \"father\": {\n" +
                "        \"name\": \"dlespinosa\",\n" +
                "        \"amount\": 2860\n" +
                "    },\n" +
                "    \"promotion\": {\n" +
                "        \"title\": \"promocion de pruebas 20 para  50\",\n" +
                "        \"sdate\": \"2017-10-25 16:10:00\",\n" +
                "        \"edate\": \"2017-11-01 23:00:00\",\n" +
                "        \"reloads\": [\n" +
                "            {\n" +
                "                \"id\": 1,\n" +
                "                \"client\": {\n" +
                "                    \"number\": \"53192289\",\n" +
                "                    \"name\": \"albertini\"\n" +
                "                },\n" +
                "                \"seller\": {\n" +
                "                    \"name\": \"amarturelo\",\n" +
                "                    \"amount\": 2860\n" +
                "                },\n" +
                "                \"count\": 1,\n" +
                "                \"amount\": 20,\n" +
                "                \"date\": \"2017-10-30 18:19:30\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 2,\n" +
                "                \"client\": {\n" +
                "                    \"number\": \"53192289\",\n" +
                "                    \"name\": \"albertini\"\n" +
                "                },\n" +
                "                \"seller\": {\n" +
                "                    \"name\": \"amarturelo\",\n" +
                "                    \"amount\": 2860\n" +
                "                },\n" +
                "                \"count\": 1,\n" +
                "                \"amount\": 20,\n" +
                "                \"date\": \"2017-10-30 18:25:01\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"user_nauta\": \"amarturelo@nauta.cu\",\n" +
                "    \"nauta_active\": true\n" +
                "}").map(s -> {
            JsonParser parser = new JsonParser();
            JsonElement mJson =  parser.parse(s.trim());
            RealmOwner owner = gson.fromJson(mJson, RealmOwner.class);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.insertOrUpdate(owner);
            realm.commitTransaction();
            realm.close();
            return owner;
        }).map(realmOwner -> owerDataMapper.transform(realmOwner))
                .doOnDispose(() -> middleware.cancel(id[0]))
                ;
    }
}
