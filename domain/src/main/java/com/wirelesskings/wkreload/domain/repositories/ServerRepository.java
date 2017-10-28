package com.wirelesskings.wkreload.domain.repositories;

import io.reactivex.Completable;

/**
 * Created by Alberto on 24/10/2017.
 */

public interface ServerRepository {
    Completable update(String nauta_mail, String nauta_password, String wk_username, String wk_password);
}
