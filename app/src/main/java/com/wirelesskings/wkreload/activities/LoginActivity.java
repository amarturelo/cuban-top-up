package com.wirelesskings.wkreload.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wirelesskings.data.cache.OwnerCacheImp;
import com.wirelesskings.data.model.internal.RealmCredentials;
import com.wirelesskings.data.model.internal.RealmServerConfig;
import com.wirelesskings.data.model.mapper.FatherDataMapper;
import com.wirelesskings.data.model.mapper.OwerDataMapper;
import com.wirelesskings.data.model.mapper.PromotionDataMapper;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.data.repositories.ServerRepositoryImpl;
import com.wirelesskings.wkreload.CredentialsStore;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.dialogs.LoadingDialog;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.internal.Credentials;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.LoginFragment;
import com.wirelesskings.wkreload.fragments.SettingsFragment;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Constants;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;


public class LoginActivity extends AppCompatActivity implements LoginContract.View,
        LoginFragment.OnLoginFragmentListener,
        SettingsFragment.OnFragmentSettingsListened {

    private LoginPresenter loginPresenter;

    private int mode = 0;

    private LoadingDialog loadingDialog;

    private CredentialsStore credentialsStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        credentialsStore = new CredentialsStore();
        initComponents();

        if (credentialsStore.hasCredentials()) {
            RealmServerConfig realmServerConfig = credentialsStore.getCredentials();
            showLogin(realmServerConfig.getEmail(), realmServerConfig.getPassword());
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
        mode = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, SettingsFragment.newInstance()).commit();
    }

    private void showLogin(String email, String password) {
        mode = 1;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, LoginFragment.newInstance(email, password)).commit();
        initPresenter(email, password);
    }

    private void initPresenter(String email, String password) {
        Setting out = new Setting(email, password);
        out.setServerType(Constants.SMTP_PLAIN); //0 for plain , 1 for ssl
        out.setHost("smtp.nauta.cu");
        out.setPort(Constants.SMTP_PLAIN_PORT); //25 for smtp plain,465 for smtp ssl.


        Setting in = new Setting(email, password);
        in.setServerType(Constants.IMAP_PLAIN);
        in.setHost("imap.nauta.cu");
        in.setPort(Constants.IMAP_PLAIN_PORT);

        loginPresenter = new LoginPresenter(
                JobExecutor.getInstance(),
                new ServerInteractor(
                        new ServerRepositoryImpl(
                                new Middleware(
                                        in, out
                                ),
                                new OwerDataMapper(
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
        loadingDialog.show(() -> loginPresenter.cancel());
    }

    @Override
    public void loginComplete() {
        saveCredentials();
        goToMain();
    }

    private void saveCredentials() {
        RealmServerConfig realmServerConfig = new RealmServerConfig()
                .setEmail(serverConfig.getEmail())
                .setPassword(serverConfig.getPassword())
                .setRealmCredentials(new RealmCredentials()
                        .setUsername(serverConfig.getCredentials().getUsername())
                        .setPassword(serverConfig.getCredentials().getPassword())
                        .setToken(serverConfig.getCredentials().getToken()));

        credentialsStore.put(realmServerConfig);
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

    ServerConfig serverConfig;

    private void doLogin(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        RealmServerConfig realmServerConfig = credentialsStore.getCredentials();
        ServerConfig local = null;
        if (realmServerConfig != null)
            local = new ServerConfig()
                    .setEmail(realmServerConfig.getEmail())
                    .setPassword(realmServerConfig.getPassword())
                    .setCredentials(new Credentials()
                            .setUsername(realmServerConfig
                                    .getRealmCredentials()
                                    .getUsername())
                            .setPassword(realmServerConfig
                                    .getRealmCredentials()
                                    .getPassword())
                            .setToken(realmServerConfig
                                    .getRealmCredentials()
                                    .getToken()));


        if (local != null && local.equals(serverConfig))
            goToMain();
        else {
            loginPresenter.update(serverConfig.getEmail(),
                    serverConfig.getCredentials().getUsername(),
                    serverConfig.getCredentials().getPassword(),
                    serverConfig.getCredentials().getToken());
        }
    }

    @Override
    public void onSettingsCallback(String email, String password) {
        showLogin(email, password);
    }
}
