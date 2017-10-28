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
import com.wirelesskings.wkreload.adapter.DividerItemDecoration;
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
        reloadList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        List<ReloadItem> reloadItems = new ArrayList<>();

        reloadItems.add(new ReloadItem()
                .setId("45")
                .setStatus(ReloadItem.STATUS.INPROGRESS)
                .setAmount(20)
                .setClientName("Alberto")
                .setClientNumber("52455665")
                .setCount(2));

        reloadItems.add(new ReloadItem()
                .setId("46")
                .setStatus(ReloadItem.STATUS.SEND)
                .setAmount(40)
                .setClientName("Denis")
                .setClientNumber("52854578")
                .setCount(2));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));

        reloadItems.add(new ReloadItem()
                .setId("89")
                .setStatus(ReloadItem.STATUS.SUCCESS)
                .setAmount(50)
                .setClientName("Josue")
                .setClientNumber("53568947")
                .setCount(1));


        ReloadAdapterRecyclerView reloadAdapterRecyclerView = new ReloadAdapterRecyclerView(reloadItems);
        reloadList.setAdapter(reloadAdapterRecyclerView);

    }
}
