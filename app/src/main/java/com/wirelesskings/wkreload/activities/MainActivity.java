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
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.adapter.PromotionsListSpinnerAdapter;
import com.wirelesskings.wkreload.dialogs.FilterBottomDialog;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.interactors.FatherInteractor;
import com.wirelesskings.wkreload.domain.interactors.PromotionInteractor;
import com.wirelesskings.wkreload.domain.model.Father;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.LoadingDialogFragment;
import com.wirelesskings.wkreload.fragments.ReloadsFragment;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorToSendException;
import com.wirelesskings.wkreload.model.FatherModel;
import com.wirelesskings.wkreload.model.PromotionItemModel;
import com.wirelesskings.wkreload.navigation.Navigator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private FilterBottomDialog filterBottomDialog;

    private TextView tvDebit;
    private TextView tvFatherName;
    private TextView tvFatherCost;

    private Spinner spinner;

    FloatingActionButton fabReload;
    FloatingActionButton fabFilter;

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
        presenter.getAllPromotions(WK.getInstance().getCredentials().getCredentials().getUsername());
        presenter.getFatherByUser(WK.getInstance().getCredentials().getCredentials().getUsername());
    }

    private void initComponents() {
        spinner = (Spinner) findViewById(R.id.spinner);


        filterBottomDialog = new FilterBottomDialog(this);

        fabReload = (FloatingActionButton) findViewById(R.id.fab_reload);
        fabFilter = (FloatingActionButton) findViewById(R.id.fab_filter);
        tvDebit = (TextView) findViewById(R.id.tv_debit);
        tvFatherName = (TextView) findViewById(R.id.tv_father);
        tvFatherCost = (TextView) findViewById(R.id.tv_cost);

        fabReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReload();
            }
        });
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });
    }

    private void showFilter() {
        filterBottomDialog.show();
    }

    private void showReload() {
        /*List<WKSDK.ReloadParams> wkReloads = new ArrayList<>();
        wkReloads.add(new WKSDK.ReloadParams()
                .setName("Yepeto")
                .setNumber("52950107")
                .setAmount(50)
                .setCount(1));
        wkReloads.add(new WKSDK.ReloadParams()
                .setName("Rosendo")
                .setNumber("53548789")
                .setAmount(20)
                .setCount(1));
        WK.getInstance().getWKSessionDefault()
                .reload(wkReloads)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();*/
        goToReload();
        //reloadBottomDialog.show();
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
        if (id == R.id.action_settings) {
            return true;
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
        Navigator.goToLogin(getApplicationContext());
        finish();
    }

    private LoadingDialogFragment loadingDialogFragment;

    @Override
    public void showLoading() {
        loadingDialogFragment = LoadingDialogFragment.newInstance();
        loadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.class.getSimpleName());
    }

    @Override
    public void hideLoading() {
        loadingDialogFragment.dismiss();
    }

    @Override
    public void showError(Exception e) {
        hideLoading();

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
        promotionsListSpinnerAdapter = new PromotionsListSpinnerAdapter(getApplicationContext(), promotionItemModels)
        ;
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
}
