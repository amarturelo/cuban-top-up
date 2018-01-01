package com.wirelesskings.data.model.mapper;

import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.wkreload.domain.model.Client;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.model.Seller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadDataMapper  {

    public static List<Reload> transform(List<RealmReload> realmReloads) {
        List<Reload> reloads = new ArrayList<>();
        if (realmReloads != null) {
            for (RealmReload realmReload :
                    realmReloads) {
                reloads.add(transform(realmReload));
            }
        }
        return reloads;
    }

    public static Reload transform(RealmReload realmReload) {
        Reload reload = null;
        if (realmReload != null) {
            reload = new Reload()
                    .setApp(realmReload.getApp())
                    .setAmount(realmReload.getAmount())
                    .setClient(new Client()
                            .setName(realmReload.getRealmClient().getName())
                            .setNumber(realmReload.getRealmClient().getNumber()))
                    .setCount(realmReload.getCount());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                reload.setDate(format.parse(realmReload.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            reload.setId(realmReload.getId())
                    .setStatus(realmReload.getStatus())
                    .setSeller(new Seller()
                            .setName(realmReload.getRealmSeller().getName())
                            .setAmount(realmReload.getRealmSeller().getAmount())
                    );
        }
        return reload;
    }
}
