package com.wirelesskings.wkreload.domain.repositories;

import com.wirelesskings.wkreload.domain.model.Owner;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Alberto on 24/10/2017.
 */

public interface ServerRepository {
    Single<Owner> update(String nauta_mail, String wk_username, String wk_password);

    Single<Owner> reload(String wk_user, String wk_pass, String nauta_user, String client_name, String client_number, String reload_count, String reload_amount);
}
