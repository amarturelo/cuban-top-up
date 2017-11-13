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
import com.wirelesskings.data.model.mapper.OwnerDataMapper;
import com.wirelesskings.data.model.mapper.PromotionDataMapper;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.ServerRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.custom.MultiStateView;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadBottomDialog implements ReloadContract.View {

    private final Button buttonOk;

    @Override
    public void hideLoading() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        bottomDialog.dismiss();
    }

    @Override
    public void loading() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        multiStateView.getView(MultiStateView.VIEW_STATE_LOADING).findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        bottomDialog.dismiss();
        bottomDialog = bottomDialog.getBuilder()
                .setTitle(R.string.waiting)
                .setContent(R.string.loading_content)
                .setCancelable(false)
                .build();
        bottomDialog.show();
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
                                new OwnerDataMapper(
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

        buttonCancel = v.findViewById(R.id.btn_cancel);
        buttonOk = v.findViewById(R.id.btn_ok);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        clientName = v.findViewById(R.id.client_name);
        clientNumber = v.findViewById(R.id.client_number);
        spAmount = v.findViewById(R.id.sp_amount);
        spCount = v.findViewById(R.id.sp_count);
        multiStateView = v.findViewById(R.id.multiStateView);

        final ServerConfig serverConfig = WK.getInstance().getCredentials();

        buttonOk.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    presenter.onReload(
                            serverConfig.getCredentials().getUsername(),
                            serverConfig.getCredentials().getPassword(),
                            serverConfig.getEmail(),
                            clientName.getText().toString().trim(),
                            clientNumber.getText().toString().trim(),
                            spCount.getSelectedItem().toString(),
                            spAmount.getSelectedItem().toString());
                    loading();
                }
            }
        });

    }

    private void cancel() {
        hideLoading();
        presenter.cancel();
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
