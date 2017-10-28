package com.wirelesskings.wkreload.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.ReloadRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.custom.MultiStateView;
import com.wirelesskings.wkreload.domain.interactors.ReloadsInteractor;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadBottomDialog implements ReloadContract.View {

    private final Button buttonOk;

    @Override
    public void loading() {
        buttonOk.setVisibility(View.GONE);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void complete() {
        bottomDialog.dismiss();
        buttonOk.setVisibility(View.VISIBLE);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    @Override
    public void error(Throwable throwable) {

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
        v = LayoutInflater.from(context).inflate(R.layout.layout_recharge, null);

        presenter = new ReloadPresenter(
                new ReloadsInteractor(
                        new ReloadRepositoryImpl(
                                new ReloadDataMapper()
                        )
                )
        );

        presenter.bindView(this);

        bottomDialog = new BottomDialog.Builder(context)
                .setTitle("Nueva recarga")
                .setContent("What can we improve? Your feedback is always welcome.")
                .setCustomView(v)
                .setCancelable(false)
                .build();

        buttonCancel = v.findViewById(R.id.btn_cancel);
        buttonOk = v.findViewById(R.id.btn_ok);

        buttonCancel.setOnClickListener(v1 -> {
                    complete();
                }
        );
        clientName = v.findViewById(R.id.client_name);
        clientNumber = v.findViewById(R.id.client_number);
        spAmount = v.findViewById(R.id.sp_amount);
        spCount = v.findViewById(R.id.sp_count);
        multiStateView = v.findViewById(R.id.multiStateView);

        buttonOk.findViewById(R.id.btn_ok).setOnClickListener(v1 -> {
            if (check()) {
                /*presenter.onReload(clientName.getText().toString().trim(),
                        clientNumber.getText().toString().trim(),
                        50, 2);*/
                loading();
            }
        });

    }

    public void show() {
        bottomDialog.show();
    }

    private boolean check() {
        return true;
    }
}
