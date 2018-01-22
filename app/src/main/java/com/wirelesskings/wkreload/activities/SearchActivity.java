package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.adapter.ReloadAdapterRecyclerView;
import com.wirelesskings.wkreload.domain.filter.Filter;
import com.wirelesskings.wkreload.fragments.ViewReloadDialogFragment;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements ReloadAdapterRecyclerView.Listened {

    public static String ARG_FILTERS = "arg_filtes";

    private RecyclerView rvReloads;

    private ReloadAdapterRecyclerView reloadAdapterRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvReloads = (RecyclerView) findViewById(R.id.rv_reloads);
        rvReloads.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        reloadAdapterRecyclerView = new ReloadAdapterRecyclerView(this);
        rvReloads.setAdapter(reloadAdapterRecyclerView);
    }

    public static Intent getCallingIntent(Context context, ArrayList<Filter> filters) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_FILTERS, filters);
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onClickItem(String id) {
        ViewReloadDialogFragment viewReloadDialogFragment = ViewReloadDialogFragment.newInstance(id);
        viewReloadDialogFragment.show(getSupportFragmentManager(), ViewReloadDialogFragment.class.getSimpleName());
    }
}
