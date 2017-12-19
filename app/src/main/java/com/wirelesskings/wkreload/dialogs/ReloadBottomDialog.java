package com.wirelesskings.wkreload.dialogs;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.data.cache.OwnerCacheImp;
import com.wirelesskings.data.model.mapper.FatherDataMapper;
import com.wirelesskings.data.model.mapper.OwnerDataMapper;
import com.wirelesskings.data.model.mapper.PromotionDataMapper;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.ServerRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.custom.MultiStateView;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorToSendException;

import java.util.List;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadBottomDialog {

    private final Button buttonOk;

    private MultiStateView multiStateView;

    private BottomDialog bottomDialog;

    private TextView clientName;

    private TextView clientNumber;

    private Spinner spAmount;

    private Spinner spCount;

    private View v;

    public interface OnDialogReloadListened {
        void onReloads(List<Reload> reloadList);
    }

    private OnDialogReloadListened dialogReloadListened;

    public void setDialogReloadListened(OnDialogReloadListened dialogReloadListened) {
        this.dialogReloadListened = dialogReloadListened;
    }

    public ReloadBottomDialog(Context context) {
        v = LayoutInflater.from(context).inflate(R.layout.layout_view_recharge, null);

        buttonOk = v.findViewById(R.id.btn_ok);
        clientName = v.findViewById(R.id.client_name);
        clientNumber = v.findViewById(R.id.client_number);
        spAmount = v.findViewById(R.id.sp_amount);
        spCount = v.findViewById(R.id.sp_count);

        buttonOk.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    if (dialogReloadListened != null) {
                    }
                }
            }
        });

    }

    private void cancel() {
    }

    public void show() {
        bottomDialog = new BottomDialog.Builder(v.getContext())
                .setTitle("Nueva recarga")
                .setCustomView(v)
                .build();
        bottomDialog.show();
    }

    private boolean check() {
        boolean check = true;
        if (clientName.getText().toString().isEmpty()) {
            clientName.setError("Debe espesificar el nombre del cliente");
            check = false;
        }
        if (clientNumber.getText().toString().isEmpty()) {
            clientNumber.setError("Debe espesificar el númbero a recargar");
            check = false;
        }

        return check;
    }
}
