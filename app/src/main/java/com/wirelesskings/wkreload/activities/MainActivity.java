package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wirelesskings.data.cache.impl.FatherCacheImpl;
import com.wirelesskings.data.cache.impl.PromotionCacheImpl;
import com.wirelesskings.data.repositories.FatherRepositoryImpl;
import com.wirelesskings.data.repositories.PromotionRepositoryImpl;
import com.wirelesskings.wkreload.CacheHelper;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.adapter.PromotionsListSpinnerAdapter;
import com.wirelesskings.wkreload.custom.MultiStateView;
import com.wirelesskings.wkreload.dialogs.FilterBottomDialog;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.interactors.FatherInteractor;
import com.wirelesskings.wkreload.domain.interactors.PromotionInteractor;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.FilterDialogFragment;
import com.wirelesskings.wkreload.fragments.LoadingDialogFragment;
import com.wirelesskings.wkreload.fragments.ReloadsFragment;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorToSendException;
import com.wirelesskings.wkreload.model.FatherModel;
import com.wirelesskings.wkreload.model.PromotionItemModel;
import com.wirelesskings.wkreload.navigation.Navigator;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private FilterBottomDialog filterBottomDialog;

    private TextView tvDebit;
    private TextView tvFatherName;
    private TextView tvFatherCost;

    private Spinner spinner;

    private MultiStateView multiStateViewPromotions;

    private FloatingActionButton fabReload;

    WK wk;

    private MainPresenter presenter;


    private PromotionsListSpinnerAdapter promotionsListSpinnerAdapter;

    @Override
    public void updateComplete() {
        Toast.makeText(getApplicationContext(), R.string.toast_update_complete, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wk = WK.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        presenter = new MainPresenter(
                JobExecutor.getInstance()
                , new PromotionInteractor(
                new PromotionRepositoryImpl(
                        new PromotionCacheImpl()
                ))
                , new FatherInteractor(
                new FatherRepositoryImpl(
                        new FatherCacheImpl()
                ))
                , WK.getInstance().getWKSessionDefault()
                , new CacheHelper(
                new FatherCacheImpl()
                , new PromotionCacheImpl()
        )
        );

        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
        presenter.getAllPromotions();
        presenter.getFatherByUser();
    }

    private void initComponents() {
        spinner = (Spinner) findViewById(R.id.spinner);
        multiStateViewPromotions = (MultiStateView) findViewById(R.id.multi_state_view_promotions);

        filterBottomDialog = new FilterBottomDialog(this);

        fabReload = (FloatingActionButton) findViewById(R.id.fab_reload);
        tvDebit = (TextView) findViewById(R.id.tv_debit);
        tvFatherName = (TextView) findViewById(R.id.tv_father);
        tvFatherCost = (TextView) findViewById(R.id.tv_cost);
        fabReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReload();
            }
        });
    }

    private void showFilter() {
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance();
        filterDialogFragment.show(getSupportFragmentManager(), FilterDialogFragment.class.getSimpleName());
    }

    private void showReload() {
        goToReload();
    }

    private void goToReload() {
        Navigator.goToReload(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            showFilter();
        }
        if (id == R.id.action_update) {
            presenter.update();
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, MainActivity.class);
        return callingIntent;
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.release();
    }

    private void goToLogin() {
        Navigator.goToLogin(this);
        finish();
    }

    private LoadingDialogFragment loadingDialogFragment;

    @Override
    public void showLoading(Disposable disposable) {
        loadingDialogFragment = LoadingDialogFragment.newInstance();
        loadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.class.getSimpleName());
        loadingDialogFragment.putDisposable(disposable);
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

    @Override
    public void renderPromotionList(List<PromotionItemModel> promotionItemModels) {
        if (promotionItemModels.isEmpty()) {
            fabReload.setVisibility(View.GONE);
            multiStateViewPromotions.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            fabReload.setVisibility(View.VISIBLE);
            multiStateViewPromotions.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
        promotionsListSpinnerAdapter = new PromotionsListSpinnerAdapter(getApplicationContext(), promotionItemModels);
        spinner.setAdapter(promotionsListSpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                presenter.getPromotionById(promotionsListSpinnerAdapter.getItem(position).getId());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ReloadsFragment.newInstance(promotionsListSpinnerAdapter.getItem(position).getId()))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void renderPromotion(PromotionItemModel promotionItemModel) {
        tvDebit.setText("$" + String.valueOf(promotionItemModel.getAmount()));
    }

    @Override
    public void renderFather(FatherModel fatherModel) {
        tvFatherCost.setText("$" + String.valueOf(fatherModel.getCount()));
        tvFatherName.setText(String.valueOf(fatherModel.getName()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.release();
    }
}
