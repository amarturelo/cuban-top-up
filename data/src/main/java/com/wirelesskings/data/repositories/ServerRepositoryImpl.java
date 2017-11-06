package com.wirelesskings.data.repositories;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wirelesskings.data.cache.OwnerCache;
import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.data.model.mapper.OwnerDataMapper;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.domain.repositories.ServerRepository;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.ResultListener;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by Alberto on 24/10/2017.
 */

public class ServerRepositoryImpl implements ServerRepository {


    private Middleware middleware;

    private Gson gson;

    private OwnerDataMapper owerDataMapper;

    private OwnerCache ownerCache;

    public ServerRepositoryImpl(Middleware middleware, OwnerDataMapper owerDataMapper, OwnerCache ownerCache) {
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
        client.put("number", client_number);
        client.put("name", client_name);

        params.put("client", client);

        Map<String, Object> reload = new LinkedHashMap<>();
        reload.put("count", reload_count);
        reload.put("amount", reload_amount);

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
        }))/*Single.just("" +
                "{\n" +
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
                "            },\n" +
                "            {\n" +
                "                \"id\": 3,\n" +
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
                "                \"date\": \"2017-10-30 18:26:02\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 4,\n" +
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
                "                \"date\": \"2017-10-30 18:27:02\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 5,\n" +
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
                "                \"date\": \"2017-10-30 18:28:01\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 6,\n" +
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
                "                \"date\": \"2017-10-30 18:28:12\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 7,\n" +
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
                "                \"date\": \"2017-10-30 18:29:01\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 8,\n" +
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
                "                \"date\": \"2017-10-30 18:29:12\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 9,\n" +
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
                "                \"date\": \"2017-10-30 18:30:01\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 10,\n" +
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
                "                \"date\": \"2017-10-30 18:30:17\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 11,\n" +
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
                "                \"date\": \"2017-10-30 18:31:01\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 12,\n" +
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
                "                \"date\": \"2017-10-30 18:31:12\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 13,\n" +
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
                "                \"date\": \"2017-10-30 18:32:01\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 14,\n" +
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
                "                \"date\": \"2017-10-30 18:32:11\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 15,\n" +
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
                "                \"date\": \"2017-10-30 18:33:01\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 16,\n" +
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
                "                \"date\": \"2017-10-30 18:33:12\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 17,\n" +
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
                "                \"date\": \"2017-10-30 18:34:01\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 18,\n" +
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
                "                \"date\": \"2017-10-30 18:34:16\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 19,\n" +
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
                "                \"date\": \"2017-10-30 18:35:02\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 20,\n" +
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
                "                \"date\": \"2017-10-30 18:35:12\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 21,\n" +
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
                "                \"date\": \"2017-10-30 18:36:02\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 22,\n" +
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
                "                \"date\": \"2017-10-30 18:36:12\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 23,\n" +
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
                "                \"date\": \"2017-10-30 18:37:02\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 24,\n" +
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
                "                \"date\": \"2017-10-30 18:37:12\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 25,\n" +
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
                "                \"date\": \"2017-10-30 18:38:02\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 26,\n" +
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
                "                \"date\": \"2017-10-30 18:38:12\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 27,\n" +
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
                "                \"date\": \"2017-10-30 18:39:02\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 28,\n" +
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
                "                \"date\": \"2017-10-30 18:39:17\",\n" +
                "                \"status\": \"inprogress\",\n" +
                "                \"app\": \"mobile\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"user_nauta\": \"amarturelo@nauta.cu\",\n" +
                "    \"nauta_active\": true\n" +
                "}")*/.map(s -> {
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
