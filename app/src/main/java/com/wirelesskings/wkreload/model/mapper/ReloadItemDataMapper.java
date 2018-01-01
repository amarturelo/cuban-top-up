package com.wirelesskings.wkreload.model.mapper;

import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.model.ReloadItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadItemDataMapper  {
    public static List<ReloadItemModel> transform(List<Reload> reloads) {
        List<ReloadItemModel> reloadItems = new ArrayList<>();
        if (reloads != null) {
            for (Reload reload :
                    reloads) {
                reloadItems.add(transform(reload));
            }
        }

        return reloadItems;
    }

    public static ReloadItemModel transform(Reload reload) {
        ReloadItemModel reloadItem = null;
        if (reload != null) {
            reloadItem = new ReloadItemModel()
                    .setAmount(reload.getAmount())
                    .setClientName(reload.getClient().getName())
                    .setClientNumber(reload.getClient().getNumber())
                    .setCount(reload.getCount())
                    .setSeller(reload.getSeller().getName())
                    .setDate(reload.getDate())
                    .setId(reload.getId());
            switch (reload.getStatus()) {
                case "inprogress":
                    reloadItem.setStatus(ReloadItemModel.STATUS.INPROGRESS);
                    break;
                case "denied":
                    reloadItem.setStatus(ReloadItemModel.STATUS.DENIED);
                    break;
                case "success":
                    reloadItem.setStatus(ReloadItemModel.STATUS.SUCCESS);
                    break;
            }


        }
        return reloadItem;
    }
}
