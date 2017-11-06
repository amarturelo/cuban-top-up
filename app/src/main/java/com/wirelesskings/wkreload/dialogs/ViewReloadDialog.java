package com.wirelesskings.wkreload.dialogs;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.data.model.mapper.FatherDataMapper;
import com.wirelesskings.data.model.mapper.OwnerDataMapper;
import com.wirelesskings.data.model.mapper.PromotionDataMapper;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.OwnerRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.custom.MultiStateView;
import com.wirelesskings.wkreload.domain.interactors.OwnerInteractor;
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

        presenter = new ViewReloadPresenter(new OwnerInteractor(
                new OwnerRepositoryImpl(
                        new ReloadDataMapper(),
                        new OwnerDataMapper(
                                new FatherDataMapper(),
                                new PromotionDataMapper(
                                        new ReloadDataMapper()
                                )
                        )
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
    public void showLoading() {
        bottomDialog.dismiss();
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        multiStateView.getView(MultiStateView.VIEW_STATE_LOADING).findViewById(R.id.btn_cancel).setOnClickListener(v1 -> cancel());
        bottomDialog = bottomDialog.getBuilder()
                .setTitle(R.string.waiting)
                .setContent(R.string.loading_content)
                .setCancelable(false)
                .build();
        bottomDialog.show();
    }

    @Override
    public void hideLoading() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        bottomDialog.dismiss();
        bottomDialog = bottomDialog.getBuilder()
                .setTitle("Detalles de la recarga")
                .setContent("")
                .setCancelable(true)
                .build();
        bottomDialog.show();
    }

    private void cancel() {
        bottomDialog.dismiss();
    }

    @Override
    public void renderReload(Reload reload) {
        clientName.setText(reload.getClient().getName());
        clientNumber.setText(reload.getClient().getNumber());
        seller.setText(reload.getSeller().getName());
        sellerAmount.setText(String.valueOf(Double.valueOf(reload.getSeller().getAmount()) / 100));
        tvBuy.setText(reload.getAmount() + "x" + reload.getCount());
        tvStatusReload.setText(reload.getStatus());
        tvDateReload.setText(DateUtils.formatDateTime(v.getContext(), reload.getDate().getTime(), 0));
    }
}
