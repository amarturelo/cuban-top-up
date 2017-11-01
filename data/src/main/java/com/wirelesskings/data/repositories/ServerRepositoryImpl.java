package com.wirelesskings.data.repositories;

import com.google.gson.Gson;
import com.wirelesskings.data.model.RealmFather;
import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.data.model.RealmPromotion;
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

    public ServerRepositoryImpl(Middleware middleware) {
        this.middleware = middleware;
        gson = new Gson();
    }

    @Override
    public Single<Owner> update(String nauta_mail, String wk_username, String wk_password) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", wk_username);
        params.put("pass", wk_password);
        params.put("user_nauta", nauta_mail);

        final String[] id = new String[1];
        return Single.create(new SingleOnSubscribe<String>() {
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
        }).map(new Function<String, RealmOwner>() {
            @Override
            public RealmOwner apply(@NonNull String s) throws Exception {
                RealmOwner owner = gson.fromJson(s, RealmOwner.class);
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.insertOrUpdate(owner);
                realm.commitTransaction();
                realm.close();
                return owner;
            }
        })
                .map(new Function<RealmOwner, Owner>() {
                    @Override
                    public Owner apply(@NonNull RealmOwner realmOwner) throws Exception {
                        return null;
                    }
                })
                .doOnDispose(() -> middleware.cancel(id[0]))
                ;
    }
}
