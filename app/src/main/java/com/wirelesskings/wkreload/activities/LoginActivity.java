package com.wirelesskings.wkreload.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wirelesskings.data.model.mapper.ServerConfigDataMapper;
import com.wirelesskings.data.repositories.RealmServerConfigRepository;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.domain.interactors.ServerConfigInteractor;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.FragmentChangeManager;
import com.wirelesskings.wkreload.fragments.LoginFragment;
import com.wirelesskings.wkreload.fragments.NautaSettingsFragment;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements LoginContract.View,
        NautaSettingsFragment.OnFragmentNautaSettingsListened,
        LoginFragment.OnFragmentLoginListened {

    private LoginPresenter loginPresenter;

    private ServerConfig serverConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginPresenter = new LoginPresenter(JobExecutor.getInstance(),
                new ServerConfigInteractor(
                        new RealmServerConfigRepository(
                                new ServerConfigDataMapper())));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.bindView(this);
    }

    @Override
    public void showServerConfig(ServerConfig serverConfig) {
        if (serverConfig == null)
            showNautaConfig();
        else {
            showLogin(serverConfig.getEmail());
        }
    }

    private void showNautaConfig() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, NautaSettingsFragment.newInstance()).commit();
    }

    private void showLogin(String email) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, LoginFragment.newInstance(email)).commit();
    }

    @Override
    public void onNautaSettings(String email, String password) {
        if (serverConfig == null)
            serverConfig = new ServerConfig();
        serverConfig.setEmail(email)
                .setPassword(password);
        showLogin(serverConfig.getEmail());
    }

    @Override
    public void onLoginSettings(String email, String password) {

    }
}
