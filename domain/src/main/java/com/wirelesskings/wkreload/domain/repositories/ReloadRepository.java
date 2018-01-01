package com.wirelesskings.wkreload.domain.repositories;

import com.wirelesskings.wkreload.domain.model.Reload;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by alberto on 30/12/17.
 */

public interface ReloadRepository {
    Observable<List<Reload>> reloads();
}
