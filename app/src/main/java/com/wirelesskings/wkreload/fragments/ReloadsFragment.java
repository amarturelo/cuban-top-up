package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wirelesskings.data.cache.impl.PromotionCacheImpl;
import com.wirelesskings.data.repositories.PromotionRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.adapter.DividerItemDecoration;
import com.wirelesskings.wkreload.adapter.ReloadAdapterRecyclerView;
import com.wirelesskings.wkreload.dialogs.LoadingDialog;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.interactors.PromotionInteractor;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorToSendException;
import com.wirelesskings.wkreload.model.ReloadItemModel;
import com.wirelesskings.wkreload.navigation.Navigator;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReloadsFragment extends Fragment implements ReloadsContract.View,
        ReloadAdapterRecyclerView.Listened,
        LoadingDialog.LoadingListener {

    public static final String ARG_PROMOTION_ID = "promotion_id";

    public ReloadsFragment() {
    }

    RecyclerView reloadList;

    ReloadAdapterRecyclerView reloadAdapterRecyclerView;

    private ReloadsPresenter presenter;


    private String mPromotionId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        reloadList = view.findViewById(R.id.reload_list);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //viewReloadDialog = new ViewReloadDialog(getActivity());

        mPromotionId = getArguments().getString(ARG_PROMOTION_ID);

        presenter = new ReloadsPresenter(
                new PromotionInteractor(
                        new PromotionRepositoryImpl(
                                new PromotionCacheImpl()
                        )
                )
                , WK.getInstance().getWKSessionDefault());
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            presenter.bindView(this);
        }
    }

    public static ReloadsFragment newInstance(String promotionId) {
        Bundle args = new Bundle();
        args.putString(ARG_PROMOTION_ID, promotionId);
        ReloadsFragment fragment = new ReloadsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reloadList.setLayoutManager(new LinearLayoutManager(getContext()));
        reloadList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        reloadAdapterRecyclerView = new ReloadAdapterRecyclerView(this);
        reloadList.setAdapter(reloadAdapterRecyclerView);
        initFragment(savedInstanceState);
    }


    @Override
    public void renderInsertions(List<ReloadItemModel> reloads) {
        reloadAdapterRecyclerView.inserted(reloads);
    }

    @Override
    public void renderDeletions(List<ReloadItemModel> reloads) {
        reloadAdapterRecyclerView.deleted(reloads);
    }

    @Override
    public void renderChanges(List<ReloadItemModel> reloads) {
        reloadAdapterRecyclerView.changed(reloads);
    }

    @Override
    public void renderReloads(List<ReloadItemModel> reloads) {
        reloadAdapterRecyclerView.inserted(reloads);
    }

    @Override
    public void hideLoading() {
    }


    @Override
    public void onDetach() {
        super.onDetach();
        presenter.release();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void showError(Exception e) {
        hideLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Ocurrio un error");

        if (e instanceof NetworkErrorToSendException) {
            builder.setMessage(R.string.error_network_to_send);
        } else if (e instanceof UserInactiveWKException) {
            goToLogin();
            builder.setMessage(R.string.error_user_inactive);
        } else {
            builder.setMessage(R.string.error_unknown);
        }
        builder.show();
    }

    private void goToLogin() {
        Navigator.goToLogin(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onReloads(mPromotionId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onClickItem(String id) {
        ViewReloadDialogFragment viewReloadDialogFragment = ViewReloadDialogFragment.newInstance(id);
        viewReloadDialogFragment.show(getChildFragmentManager(), ViewReloadDialogFragment.class.getSimpleName());
    }

    @Override
    public void onCancel() {
        presenter.cancel();
    }






}
