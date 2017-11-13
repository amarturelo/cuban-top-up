package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.LoginFragment;
import com.wirelesskings.wkreload.fragments.SettingsFragment;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Constants;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;


public class LoginActivity extends AppCompatActivity implements LoginContract.View,
        LoginFragment.OnLoginFragmentListener,
        SettingsFragment.OnFragmentSettingsListened {

    private LoginPresenter loginPresenter;

    private LoadingDialog loadingDialog;

    private CredentialsStore credentialsStore;

    private WK wk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        wk = WK.getInstance();
        credentialsStore = new CredentialsStore();
        initComponents();

        if (wk.hasCredentials()) {
            showLogin(wk.getCredentials().getEmail(), wk.getCredentials().getPassword());
        } else
            showConfig();
    }

    private void initComponents() {
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void showConfig() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, SettingsFragment.newInstance()).commit();
    }

    private void showLogin(String email, String password) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, LoginFragment.newInstance(email, password)).commit();
    }

    @Override
    public void showError(Exception e) {
        hideLoading();
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
        saveCredentials();
        goToMain();
    }

    private void saveCredentials() {
        wk.saveCredentials(serverConfig);
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginCallback(ServerConfig serverConfig) {
        doLogin(serverConfig);
    }

    @Override
    public void onBackSettings() {
        showConfig();
    }

    ServerConfig serverConfig;

    private void doLogin(ServerConfig serverConfig) {
        Setting out = new Setting(serverConfig.getEmail(), serverConfig.getPassword());
        out.setServerType(Constants.SMTP_PLAIN); //0 for plain , 1 for ssl
        out.setHost("smtp.nauta.cu");
        out.setPort(Constants.SMTP_PLAIN_PORT); //25 for smtp plain,465 for smtp ssl.


        Setting in = new Setting(serverConfig.getEmail(), serverConfig.getPassword());
        in.setServerType(Constants.IMAP_PLAIN);
        in.setHost("imap.nauta.cu");
        in.setPort(Constants.IMAP_PLAIN_PORT);

        wk.setMiddleware(new Middleware(
                in, out, serverConfig.getCredentials().getToken()
        ));

        loginPresenter = new LoginPresenter(
                JobExecutor.getInstance(),
                new ServerInteractor(
                        new ServerRepositoryImpl(
                                wk.getMiddleware(),
                                new OwnerDataMapper(
                                        new FatherDataMapper(),
                                        new PromotionDataMapper(
                                                new ReloadDataMapper()
                                        )
                                ),
                                new OwnerCacheImp()
                        )
                )
        );

        loginPresenter.bindView(this);


        this.serverConfig = serverConfig;
        ServerConfig local = null;
        if (wk.hasCredentials())
            local = wk.getCredentials();
        if (local != null && local.equals(serverConfig))
            goToMain();
        else {
            loginPresenter.update(serverConfig.getEmail(),
                    serverConfig.getCredentials().getUsername(),
                    serverConfig.getCredentials().getPassword());
        }
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, LoginActivity.class);
        return callingIntent;
    }

    @Override
    public void onSettingsCallback(String email, String password) {
        showLogin(email, password);
    }
}
