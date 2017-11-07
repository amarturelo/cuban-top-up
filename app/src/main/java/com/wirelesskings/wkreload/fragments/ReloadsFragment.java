package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wirelesskings.data.cache.OwnerCacheImp;
import com.wirelesskings.data.model.mapper.FatherDataMapper;
import com.wirelesskings.data.model.mapper.OwnerDataMapper;
import com.wirelesskings.data.model.mapper.PromotionDataMapper;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.OwnerRepositoryImpl;
import com.wirelesskings.data.repositories.ServerRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.adapter.DividerItemDecoration;
import com.wirelesskings.wkreload.adapter.ReloadAdapterRecyclerView;
import com.wirelesskings.wkreload.dialogs.LoadingDialog;
import com.wirelesskings.wkreload.dialogs.ViewReloadDialog;
import com.wirelesskings.wkreload.domain.interactors.OwnerInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.model.ReloadItem;
import com.wirelesskings.wkreload.model.mapper.ReloadItemDataMapper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReloadsFragment extends Fragment implements ReloadsContract.View,
        ReloadAdapterRecyclerView.Listened,
        LoadingDialog.LoadingListener {

    public ReloadsFragment() {
    }

    @BindView(R.id.reload_list)
    RecyclerView reloadList;

    ReloadAdapterRecyclerView reloadAdapterRecyclerView;

    private ReloadsPresenter presenter;

    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        loadingDialog = new LoadingDialog(getActivity());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewReloadDialog = new ViewReloadDialog(getActivity());
        presenter = new ReloadsPresenter(
                new OwnerInteractor(
                        new OwnerRepositoryImpl(
                                new ReloadDataMapper(),
                                new OwnerDataMapper(
                                        new FatherDataMapper(),
                                        new PromotionDataMapper(
                                                new ReloadDataMapper()
                                        )
                                )
                        )
                ),
                new ReloadItemDataMapper(),
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
                ));

    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            presenter.bindView(this);
        }
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
    public void renderInsertions(List<ReloadItem> reloads) {
        reloadAdapterRecyclerView.inserted(reloads);
    }

    @Override
    public void renderDeletions(List<ReloadItem> reloads) {
        reloadAdapterRecyclerView.deleted(reloads);
    }

    @Override
    public void renderChanges(List<ReloadItem> reloads) {
        reloadAdapterRecyclerView.changed(reloads);
    }

    @Override
    public void renderReloads(List<ReloadItem> reloads) {
        reloadAdapterRecyclerView.inserted(reloads);
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void updateComplete() {
        Toast.makeText(getContext(), "Update Complete", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.release();
    }

    @Override
    public void hasPromotions(boolean b) {
        if (!b) {

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.txt_not_promotions)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();

        }
        listened.visibilityBottom(b);

    }

    @Override
    public void showLoading() {
        loadingDialog.show(this);
    }

    @Override
    public void showError(Exception e) {
        hideLoading();
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderDebit(String debit) {
        if (listened != null)
            listened.onDebit(debit);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReloadsFragmentListened)
            listened = (OnReloadsFragmentListened) context;
    }

    private OnReloadsFragmentListened listened;

    private ViewReloadDialog viewReloadDialog;

    @Override
    public void onClickItem(String id) {
        viewReloadDialog.show(id);
    }

    @Override
    public void onCancel() {
        presenter.cancel();
    }

    public interface OnReloadsFragmentListened {
        void onDebit(String debit);

        void visibilityBottom(boolean b);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                update();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void update() {
        presenter.update();
    }


}
