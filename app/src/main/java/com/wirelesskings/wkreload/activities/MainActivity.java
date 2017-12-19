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
import android.widget.TextView;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.dialogs.ReloadBottomDialog;
import com.wirelesskings.wkreload.domain.model.Father;
import com.wirelesskings.wkreload.fragments.ReloadsFragment;
import com.wirelesskings.wkreload.navigation.Navigator;

public class MainActivity extends AppCompatActivity implements ReloadsFragment.OnReloadsFragmentListened {

    private ReloadBottomDialog reloadBottomDialog;

    private TextView tvDebit;
    private TextView tvFatherName;
    private TextView tvFatherCost;

    FloatingActionButton fab;

    WK wk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wk = WK.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reloadBottomDialog = new ReloadBottomDialog(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        tvDebit = (TextView) findViewById(R.id.tv_debit);
        tvFatherName = (TextView) findViewById(R.id.tv_father);
        tvFatherCost = (TextView) findViewById(R.id.tv_cost);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReload();
            }
        });

    }

    private void showReload() {
        reloadBottomDialog.show();
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
        tvDebit.setText("$" + String.valueOf(father.getAmount()));
        tvFatherCost.setText("$" + String.valueOf(father.getCost()));
        tvFatherName.setText(String.valueOf(father.getName()));
    }
}
