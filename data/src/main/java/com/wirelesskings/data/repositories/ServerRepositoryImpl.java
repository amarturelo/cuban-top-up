package com.wirelesskings.data.repositories;

import android.util.Log;

import com.wirelesskings.wkreload.domain.repositories.ServerRepository;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.WKField;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Alberto on 24/10/2017.
 */

public class ServerRepositoryImpl implements ServerRepository {


    private Middleware middleware;

    public ServerRepositoryImpl(Middleware middleware) {
        this.middleware = middleware;
    }

    @Override
    public Completable update(String nauta_mail, String wk_username, String wk_password) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", wk_username);
        params.put("pass", wk_password);
        params.put("user_nauta", nauta_mail);

        return middleware.call("update", params).doAfterSuccess(new Consumer<Map<String, Object>>() {
            @Override
            public void accept(Map<String, Object> stringObjectMap) throws Exception {
                Log.d("","");
            }
        }).flatMapCompletable(new Function<Map<String, Object>, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull Map<String, Object> stringObjectMap) throws Exception {
                return Completable.complete();
            }
        });
    }
}
