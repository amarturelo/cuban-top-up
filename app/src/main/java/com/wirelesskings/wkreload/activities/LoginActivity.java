package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.wirelesskings.data.cache.impl.FatherCacheImpl;
import com.wirelesskings.data.cache.impl.PromotionCacheImpl;
import com.wirelesskings.wkreload.CacheHelper;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.LoadingDialogFragment;
import com.wirelesskings.wkreload.fragments.LoginFragment;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorToSendException;


public class LoginActivity extends AppCompatActivity implements LoginContract.View,
        LoginFragment.OnLoginFragmentListener {

    private LoginPresenter loginPresenter;


    private WK wk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        wk = WK.getInstance();
        initComponents();

        loginPresenter = new LoginPresenter(
                JobExecutor.getInstance()
                , new CacheHelper(
                new FatherCacheImpl()
                , new PromotionCacheImpl()
        )
        );

        initActivity(savedInstanceState);
    }

    private void initActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, LoginFragment.newInstance()).commit();
        }
    }

    private void initComponents() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.bindView(this);
    }

    @Override
    public void onGoToMain() {
        goToMain();
    }

    @Override
    public void showError(Exception e) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Ocurrio un error");

        if (e instanceof NetworkErrorToSendException) {
            builder.setMessage(R.string.error_network_to_send);
        } else if (e instanceof UserInactiveWKException) {
            builder.setMessage(R.string.error_user_inactive);
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

    private LoadingDialogFragment loadingDialogFragment;


    @Override
    public void hideLoading() {
        loadingDialogFragment.dismiss();
        loadingDialogFragment = null;
    }

    @Override
    public void showLoading() {
        loadingDialogFragment = LoadingDialogFragment.newInstance();
        loadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.class.getSimpleName());
    }

    @Override
    public void loginComplete() {
        goToMain();
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLogin(ServerConfig serverConfig) {
        doLogin(serverConfig);
    }

    private void doLogin(ServerConfig serverConfig) {
        loginPresenter.login(serverConfig);
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, LoginActivity.class);
        return callingIntent;
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginPresenter.release();
    }
}
