package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wirelesskings.data.cache.impl.FatherCacheImpl;
import com.wirelesskings.data.cache.impl.PromotionCacheImpl;
import com.wirelesskings.data.repositories.FatherRepositoryImpl;
import com.wirelesskings.data.repositories.PromotionRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.adapter.PromotionsListSpinnerAdapter;
import com.wirelesskings.wkreload.dialogs.FilterBottomDialog;
import com.wirelesskings.wkreload.domain.interactors.FatherInteractor;
import com.wirelesskings.wkreload.domain.interactors.PromotionInteractor;
import com.wirelesskings.wkreload.domain.model.Father;
import com.wirelesskings.wkreload.fragments.ReloadsFragment;
import com.wirelesskings.wkreload.model.FatherModel;
import com.wirelesskings.wkreload.model.PromotionItemModel;
import com.wirelesskings.wkreload.navigation.Navigator;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ReloadsFragment.OnReloadsFragmentListened, MainContract.View {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wk = WK.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new MainPresenter(
                new PromotionInteractor(
                        new PromotionRepositoryImpl(
                                new PromotionCacheImpl()
                        )
                )
                , new FatherInteractor(
                new FatherRepositoryImpl(
                        new FatherCacheImpl()
                )
        )
        );

        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
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
        Navigator.goToReload(getApplicationContext());
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

        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, MainActivity.class);
        return callingIntent;
    }

    @Override
    public void onInactiveUser() {
        WK.getInstance().saveCredentials(wk.getCredentials().setActive(false));
        goToLogin();
    }

    private void goToLogin() {
        Navigator.goToLogin(getApplicationContext());
        finish();
    }

    @Override
    public void onFather(Father father) {
        tvFatherCost.setText("$" + String.valueOf(father.getCost()));
        tvFatherName.setText(String.valueOf(father.getName()));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void renderPromotionList(List<PromotionItemModel> promotionItemModels) {
        promotionsListSpinnerAdapter = new PromotionsListSpinnerAdapter(getApplicationContext(), (PromotionItemModel[]) promotionItemModels.toArray());
        spinner.setAdapter(promotionsListSpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
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
