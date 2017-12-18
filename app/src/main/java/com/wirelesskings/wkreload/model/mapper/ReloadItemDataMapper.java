package com.wirelesskings.wkreload.model.mapper;

import android.text.format.DateUtils;

import com.wirelesskings.data.model.mapper.DataMapper;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.model.ReloadItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadItemDataMapper implements DataMapper<Reload, ReloadItem> {
    @Override
    public List<ReloadItem> transform(List<Reload> reloads) {
        List<ReloadItem> reloadItems = new ArrayList<>();
        if (reloads != null) {
            for (Reload reload :
                    reloads) {
                reloadItems.add(transform(reload));
            }
        }

        return reloadItems;
    }

    @Override
    public ReloadItem transform(Reload reload) {
        ReloadItem reloadItem = null;
        if (reload != null) {
            reloadItem = new ReloadItem()
                    .setAmount(reload.getAmount())
                    .setClientName(reload.getClient().getName())
                    .setClientNumber(reload.getClient().getNumber())
                    .setCount(reload.getCount())
                    .setSeller(reload.getSeller().getName())
                    .setDate(reload.getDate())
                    .setId(reload.getId());
            switch (reload.getStatus()) {
                case "inprogress":
                    reloadItem.setStatus(ReloadItem.STATUS.INPROGRESS);
                    break;
                case "denied":
                    reloadItem.setStatus(ReloadItem.STATUS.DENIED);
                    break;
                case "success":
                    reloadItem.setStatus(ReloadItem.STATUS.SUCCESS);
                    break;
            }


        }
        return reloadItem;
    }
}
