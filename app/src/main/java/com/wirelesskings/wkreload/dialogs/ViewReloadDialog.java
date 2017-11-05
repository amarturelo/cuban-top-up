package com.wirelesskings.wkreload.dialogs;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.ReloadRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.custom.MultiStateView;
import com.wirelesskings.wkreload.domain.interactors.ReloadsInteractor;
import com.wirelesskings.wkreload.domain.model.Reload;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ViewReloadDialog implements ViewReloadContract.View {
    private BottomDialog bottomDialog;

    private ViewReloadPresenter presenter;

    private TextView clientName;
    private TextView clientNumber;
    private TextView seller;
    private TextView sellerAmount;
    private TextView tvBuy;
    private TextView tvStatusReload;
    private TextView tvDateReload;
    private MultiStateView multiStateView;
    private View v;

    public ViewReloadDialog(Context context) {

        v = LayoutInflater.from(context).inflate(R.layout.layout_view_reload, null);

        bottomDialog = new BottomDialog.Builder(context)
                .setTitle("Detalles de la recarga")
                .setCustomView(v)
                .build();

        presenter = new ViewReloadPresenter(new ReloadsInteractor(
                new ReloadRepositoryImpl(
                        new ReloadDataMapper()
                )
        ));

        clientName = v.findViewById(R.id.tv_client_name);
        clientNumber = v.findViewById(R.id.tv_client_number);
        seller = v.findViewById(R.id.tv_seller);
        sellerAmount = v.findViewById(R.id.tv_seller_amount);
        multiStateView = v.findViewById(R.id.multiStateView);
        tvBuy = v.findViewById(R.id.tv_buy);
        tvStatusReload = v.findViewById(R.id.tv_status_reload);
        tvDateReload = v.findViewById(R.id.tv_date_reload);

        presenter.bindView(this);
    }

    public void show(String reloadId) {
        bottomDialog.show();
        presenter.onViewReload(reloadId);
    }

    @Override
    public void loading() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void renderReload(Reload reload) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        clientName.setText(reload.getClient().getName());
        clientNumber.setText(reload.getClient().getNumber());
        seller.setText(reload.getSeller().getName());
        sellerAmount.setText(String.valueOf(Double.valueOf(reload.getSeller().getAmount()) / 100));
        tvBuy.setText(reload.getAmount() + "x" + reload.getCount());
        tvStatusReload.setText(reload.getStatus());
        tvDateReload.setText(DateUtils.formatDateTime(v.getContext(), reload.getDate().getTime(), 0));
    }
}
