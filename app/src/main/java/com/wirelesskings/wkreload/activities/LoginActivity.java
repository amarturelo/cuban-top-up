package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.wirelesskings.data.cache.OwnerCacheImp;
import com.wirelesskings.data.model.mapper.FatherDataMapper;
import com.wirelesskings.data.model.mapper.OwnerDataMapper;
import com.wirelesskings.data.model.mapper.PromotionDataMapper;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.ServerRepositoryImpl;
import com.wirelesskings.wkreload.CredentialsStore;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.dialogs.LoadingDialog;
import com.wirelesskings.wkreload.domain.exceptions.UserInactiveWKException;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.LoginFragment;
import com.wirelesskings.wkreload.fragments.SettingsFragment;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorToSendException;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Constants;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;
import com.wirelesskings.wkreload.navigation.Navigator;


public class LoginActivity extends AppCompatActivity implements LoginContract.View,
        LoginFragment.OnLoginFragmentListener {

    private LoginPresenter loginPresenter;

    private LoadingDialog loadingDialog;

    private WK wk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        wk = WK.getInstance();
        initComponents();

        loginPresenter = new LoginPresenter(
                JobExecutor.getInstance()
        );

        initActivity(savedInstanceState);
    }

    private void initActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, LoginFragment.newInstance()).commit();
        }
    }

    private void initComponents() {
        loadingDialog = new LoadingDialog(this);
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

        hideLoading();
        Snackbar snackbar = null;
        if (e instanceof NetworkErrorToSendException) {
            snackbar = Snackbar
                    .make(findViewById(R.id.coordinator), R.string.error_network_to_send, Snackbar.LENGTH_INDEFINITE);
        } else if (e instanceof UserInactiveWKException) {
            snackbar = Snackbar
                    .make(findViewById(R.id.coordinator), R.string.error_user_inactive, Snackbar.LENGTH_INDEFINITE);
        } else {
            snackbar = Snackbar
                    .make(findViewById(R.id.coordinator), R.string.error_unknown, Snackbar.LENGTH_INDEFINITE);
        }
        snackbar.show();
    }


    private void goToLogin() {
        Navigator.goToLogin(getApplicationContext());
        finish();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showLoading() {
        loadingDialog.show(new LoadingDialog.LoadingListener() {
            @Override
            public void onCancel() {
                loginPresenter.cancel();
            }
        });
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
