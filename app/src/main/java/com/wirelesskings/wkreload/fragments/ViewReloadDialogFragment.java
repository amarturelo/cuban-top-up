package com.wirelesskings.wkreload.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wirelesskings.data.cache.impl.ReloadCacheImpl;
import com.wirelesskings.data.repositories.ReloadRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.domain.interactors.ReloadInteractor;
import com.wirelesskings.wkreload.domain.model.Reload;

/**
 * Created by alberto on 2/01/18.
 */

public class ViewReloadDialogFragment extends BottomSheetDialogFragment implements ViewReloadContract.View {

    private static final String ARG_RELOAD_ID = "reload_id";

    private ViewReloadPresenter presenter;

    private TextView clientName;
    private TextView clientNumber;
    private TextView seller;
    private TextView sellerAmount;
    private TextView tvBuy;
    private TextView tvStatusReload;
    private TextView tvDateReload;

    private String mReloadId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mReloadId = getArguments().getString(ARG_RELOAD_ID);
        presenter = new ViewReloadPresenter(new ReloadInteractor(
                new ReloadRepositoryImpl(
                        new ReloadCacheImpl()
                )
        ));
        return super.onCreateDialog(savedInstanceState);
    }

    // TODO: Customize parameters
    public static ViewReloadDialogFragment newInstance(String reloadId) {
        final ViewReloadDialogFragment fragment = new ViewReloadDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_RELOAD_ID, reloadId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_view_reload, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clientName = view.findViewById(R.id.tv_client_name);
        clientNumber = view.findViewById(R.id.tv_client_number);
        seller = view.findViewById(R.id.tv_seller);
        sellerAmount = view.findViewById(R.id.tv_seller_amount);
        tvBuy = view.findViewById(R.id.tv_buy);
        tvStatusReload = view.findViewById(R.id.tv_status_reload);
        tvDateReload = view.findViewById(R.id.tv_date_reload);
        presenter.bindView(this);

        presenter.onViewReload(mReloadId);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void renderReload(Reload reload) {
        clientName.setText(reload.getClient().getName());
        clientNumber.setText(reload.getClient().getNumber());
        seller.setText(reload.getSeller().getName());
        sellerAmount.setText(String.valueOf(Double.valueOf(reload.getSeller().getAmount()) / 10));
        tvBuy.setText(reload.getAmount() + "x" + reload.getCount());
        tvStatusReload.setText(reload.getStatus());
        tvDateReload.setText(DateUtils.formatDateTime(getContext(), reload.getDate().getTime(), 0));
    }
}
