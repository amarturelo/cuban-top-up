package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.fragments.ReloadOptionItemListDialogFragment;


public class ReloadActivity extends AppCompatActivity implements ReloadContract.View {

    private ReloadPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_reload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        presenter = new ReloadPresenter();
        presenter.bindView(this);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ReloadActivity.class);
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
}
