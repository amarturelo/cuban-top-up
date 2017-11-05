package com.wirelesskings.wkreload.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.data.cache.OwnerCacheImp;
import com.wirelesskings.data.model.mapper.FatherDataMapper;
import com.wirelesskings.data.model.mapper.OwerDataMapper;
import com.wirelesskings.data.model.mapper.PromotionDataMapper;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.ReloadRepositoryImpl;
import com.wirelesskings.data.repositories.ServerRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.custom.MultiStateView;
import com.wirelesskings.wkreload.domain.interactors.ReloadsInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadBottomDialog implements ReloadContract.View {

    private final Button buttonOk;

    @Override
    public void hideLoading() {
        bottomDialog.dismiss();
        buttonOk.setVisibility(View.VISIBLE);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    @Override
    public void loading() {
        buttonOk.setVisibility(View.GONE);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void complete() {
        hideLoading();
    }

    @Override
    public void error(Throwable throwable) {
        hideLoading();
        Toast.makeText(v.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private MultiStateView multiStateView;

    private BottomDialog bottomDialog;

    private TextView clientName;

    private TextView clientNumber;

    private Spinner spAmount;

    private Spinner spCount;

    private View v;

    private ReloadPresenter presenter;

    private Button buttonCancel;

    public ReloadBottomDialog(Context context) {
        v = LayoutInflater.from(context).inflate(R.layout.layout_view_recharge, null);

        presenter = new ReloadPresenter(
                new ServerInteractor(
                        new ServerRepositoryImpl(
                                WK.getInstance().getMiddleware(),
                                new OwerDataMapper(
                                        new FatherDataMapper(),
                                        new PromotionDataMapper(
                                                new ReloadDataMapper()
                                        )
                                ),
                                new OwnerCacheImp()
                        )
                )
        );

        presenter.bindView(this);

        bottomDialog = new BottomDialog.Builder(context)
                .setTitle("Nueva recarga")
                .setCustomView(v)
                .setCancelable(false)
                .build();

        buttonCancel = v.findViewById(R.id.btn_cancel);
        buttonOk = v.findViewById(R.id.btn_ok);

        buttonCancel.setOnClickListener(v1 -> cancel()
        );
        clientName = v.findViewById(R.id.client_name);
        clientNumber = v.findViewById(R.id.client_number);
        spAmount = v.findViewById(R.id.sp_amount);
        spCount = v.findViewById(R.id.sp_count);
        multiStateView = v.findViewById(R.id.multiStateView);

        ServerConfig serverConfig = WK.getInstance().getCredentials();

        buttonOk.findViewById(R.id.btn_ok).setOnClickListener(v1 -> {
            if (check()) {
                presenter.onReload(
                        serverConfig.getCredentials().getUsername(),
                        serverConfig.getCredentials().getPassword(),
                        serverConfig.getEmail(),
                        clientName.getText().toString().trim(),
                        clientNumber.getText().toString().trim(),
                        spAmount.getSelectedItem().toString(), spCount.getSelectedItem().toString());
                loading();
            }
        });

    }

    private void cancel() {
        hideLoading();
        presenter.cancel();
    }

    public void show() {
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
