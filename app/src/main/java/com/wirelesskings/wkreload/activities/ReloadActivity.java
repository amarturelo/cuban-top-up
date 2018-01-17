package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wirelesskings.data.cache.impl.FatherCacheImpl;
import com.wirelesskings.data.cache.impl.PromotionCacheImpl;
import com.wirelesskings.wkreload.CacheHelper;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.adapter.DividerItemDecoration;
import com.wirelesskings.wkreload.adapter.PreReloadAdapterRecyclerView;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.LoadingDialogFragment;
import com.wirelesskings.wkreload.fragments.ReloadClickItemMenuDialogFragment;
import com.wirelesskings.wkreload.fragments.ReloadDialogFragment;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorToSendException;
import com.wirelesskings.wkreload.model.PreReloadItemModel;
import com.wirelesskings.wkreload.navigation.Navigator;

import io.reactivex.disposables.Disposable;


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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        listPreReload.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));

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
                , JobExecutor.getInstance()
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

    private LoadingDialogFragment loadingDialogFragment;

    @Override
    public void showLoading(Disposable disposable) {
        loadingDialogFragment = LoadingDialogFragment.newInstance();
        loadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.class.getSimpleName());
    }

    @Override
    public void hideLoading() {
        loadingDialogFragment.dismiss();
    }

    @Override
    public void showError(Exception e) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Ocurrio un error");

        if (e instanceof NetworkErrorToSendException) {
            builder.setMessage(R.string.error_network_to_send);
        } else if (e instanceof UserInactiveWKException) {
            builder.setMessage(R.string.error_user_inactive);
            goToLogin();
        } else {
            builder.setMessage(R.string.error_unknown);
        }
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void goToLogin() {
        Navigator.goToLogin(this);
        finish();
    }

    @Override
    public void reloadComplete() {
        preReloadAdapterRecyclerView.clear();
        checkCount();
        Toast.makeText(getApplicationContext(), R.string.toast_reload_complete, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.release();
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
