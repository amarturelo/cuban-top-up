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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wirelesskings.data.cache.impl.ReloadCacheImpl;
import com.wirelesskings.data.repositories.ReloadRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.adapter.ReloadAdapterRecyclerView;
import com.wirelesskings.wkreload.custom.MultiStateView;
import com.wirelesskings.wkreload.domain.filter.Filter;
import com.wirelesskings.wkreload.domain.interactors.ReloadInteractor;
import com.wirelesskings.wkreload.fragments.ViewReloadDialogFragment;
import com.wirelesskings.wkreload.model.ReloadItemModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ReloadAdapterRecyclerView.Listened, SearchContract.View {

    public static String ARG_FILTERS = "arg_filters";

    private RecyclerView rvReloads;

    private ReloadAdapterRecyclerView reloadAdapterRecyclerView;

    private SearchPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new SearchPresenter(new ReloadInteractor(
                new ReloadRepositoryImpl(
                        new ReloadCacheImpl()
                )
        ));

        rvReloads = (RecyclerView) findViewById(R.id.rv_reloads);
        rvReloads.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        reloadAdapterRecyclerView = new ReloadAdapterRecyclerView(this);
        rvReloads.setAdapter(reloadAdapterRecyclerView);
        presenter.bindView(this);
        initActivity(savedInstanceState);
    }

    private void initActivity(Bundle savedInstanceState) {
        if (getIntent().getParcelableArrayListExtra(ARG_FILTERS) != null)
            presenter.onReloads(getIntent().<Filter>getParcelableArrayListExtra(ARG_FILTERS));
        else
            presenter.onReloads(new ArrayList<Filter>());
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

    @Override
    public void renderReloads(List<ReloadItemModel> reloads) {
        reloadAdapterRecyclerView.inserted(reloads);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(Exception e) {
        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }
}
