package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.ReloadRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.adapter.DividerItemDecoration;
import com.wirelesskings.wkreload.adapter.ReloadAdapterRecyclerView;
import com.wirelesskings.wkreload.dialogs.ViewReloadDialog;
import com.wirelesskings.wkreload.domain.interactors.ReloadsInteractor;
import com.wirelesskings.wkreload.model.ReloadItem;
import com.wirelesskings.wkreload.model.mapper.ReloadItemDataMapper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReloadsFragment extends Fragment implements ReloadsContract.View, ReloadAdapterRecyclerView.Listened {

    public ReloadsFragment() {
    }

    @BindView(R.id.reload_list)
    RecyclerView reloadList;

    ReloadAdapterRecyclerView reloadAdapterRecyclerView;

    private ReloadsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewReloadDialog = new ViewReloadDialog(getActivity());
        presenter = new ReloadsPresenter(
                new ReloadsInteractor(
                        new ReloadRepositoryImpl(
                                new ReloadDataMapper()
                        )
                ),
                new ReloadItemDataMapper());

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
    public void renderDebit(long debit) {
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

    public interface OnReloadsFragmentListened {
        void onDebit(long debit);
    }
}
