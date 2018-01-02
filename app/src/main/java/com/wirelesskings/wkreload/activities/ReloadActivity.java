package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wirelesskings.data.cache.impl.FatherCacheImpl;
import com.wirelesskings.data.cache.impl.PromotionCacheImpl;
import com.wirelesskings.wkreload.CacheHelper;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.adapter.PreReloadAdapterRecyclerView;
import com.wirelesskings.wkreload.dialogs.LoadingDialog;
import com.wirelesskings.wkreload.fragments.ReloadClickItemMenuDialogFragment;
import com.wirelesskings.wkreload.fragments.ReloadDialogFragment;
import com.wirelesskings.wkreload.model.PreReloadItemModel;


public class ReloadActivity extends AppCompatActivity implements ReloadContract.View, ReloadDialogFragment.Listener, ReloadClickItemMenuDialogFragment.Listener {

    private ReloadPresenter presenter;

    private RecyclerView listPreReload;

    private View reloadBottom;

    private PreReloadAdapterRecyclerView preReloadAdapterRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_reload);

        listPreReload = (RecyclerView) findViewById(R.id.list_pre_reload);
        reloadBottom = findViewById(R.id.reload_bottom);
        reloadBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        listPreReload.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listPreReload.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        preReloadAdapterRecyclerView = new PreReloadAdapterRecyclerView(new PreReloadAdapterRecyclerView.Listened() {
            @Override
            public void onClickItem(View view, int id) {
                ReloadClickItemMenuDialogFragment reloadClickItemMenuDialogFragment = ReloadClickItemMenuDialogFragment.newInstance(id);
                reloadClickItemMenuDialogFragment.show(getSupportFragmentManager(), ReloadClickItemMenuDialogFragment.class.getSimpleName());
            }
        });

        listPreReload.setAdapter(preReloadAdapterRecyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReload();
            }
        });

        presenter = new ReloadPresenter(WK.getInstance().getWKSessionDefault()
                , new CacheHelper(
                new FatherCacheImpl()
                , new PromotionCacheImpl()
        ));
        presenter.bindView(this);
    }

    private void reload() {
        presenter.reload(preReloadAdapterRecyclerView.getItems());
    }

    private void showReload() {
        ReloadDialogFragment reloadDialogFragment = ReloadDialogFragment.newInstance(50);
        reloadDialogFragment.show(getSupportFragmentManager(), ReloadDialogFragment.class.getSimpleName());
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ReloadActivity.class);
    }

    @Override
    public void showLoading() {
        LoadingDialog loadingDialog = new LoadingDialog(getApplicationContext());
        loadingDialog.show(new LoadingDialog.LoadingListener() {
            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void reloadComplete() {

    }

    @Override
    public void onReload(PreReloadItemModel reloadParams) {
        preReloadAdapterRecyclerView.add(reloadParams);
        checkCount();
    }

    private void checkCount() {
        if (preReloadAdapterRecyclerView.getItemCount() == 0)
            reloadBottom.setVisibility(View.GONE);
        else
            reloadBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemRemove(int anInt) {
        preReloadAdapterRecyclerView.deleted(anInt);
        checkCount();
    }
}
