package com.wirelesskings.wkreload.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.adapter.ReloadAdapterRecyclerView;
import com.wirelesskings.wkreload.model.ReloadItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @BindView(R.id.reload_list)
    RecyclerView reloadList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reloadList.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ReloadItem> reloadItems = new ArrayList<>();

        reloadItems.add(new ReloadItem("45", "Denis", 20, 2, "52950107", "Alberto"));
        reloadItems.add(new ReloadItem("45", "Lazaro", 40, 1, "52858552", "Rosendo"));
        reloadItems.add(new ReloadItem("45", "Denis", 15, 2, "52950107", "Alberto"));
        reloadItems.add(new ReloadItem("45", "Adriana", 50, 4, "52659889", "Alberto"));
        reloadItems.add(new ReloadItem("45", "Nelsy", 40, 3, "52950107", "Alberto"));
        reloadItems.add(new ReloadItem("45", "Yslen", 20, 1, "52950107", "Alberto"));
        reloadItems.add(new ReloadItem("45", "Richard", 10, 3, "52950107", "Alberto"));
        reloadItems.add(new ReloadItem("45", "Robert", 20, 2, "52950107", "Alberto"));

        ReloadAdapterRecyclerView reloadAdapterRecyclerView = new ReloadAdapterRecyclerView(reloadItems);
        reloadList.setAdapter(reloadAdapterRecyclerView);

    }
}
