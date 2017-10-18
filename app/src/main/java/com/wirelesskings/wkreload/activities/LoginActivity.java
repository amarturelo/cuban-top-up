package com.wirelesskings.wkreload.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wirelesskings.data.model.mapper.ServerConfigDataMapper;
import com.wirelesskings.data.repositories.RealmServerConfigRepository;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.domain.interactors.ServerConfigInteractor;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.FragmentChangeManager;
import com.wirelesskings.wkreload.fragments.LoginFragment;
import com.wirelesskings.wkreload.fragments.NautaSettingsFragment;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private FragmentChangeManager fragmentChangeManager;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(NautaSettingsFragment.newInstance());
        fragments.add(LoginFragment.newInstance());

        fragmentChangeManager = new FragmentChangeManager(getSupportFragmentManager(), R.id.fragment, (ArrayList<Fragment>) fragments);

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
    public void hasServerSettings() {
        fragmentChangeManager.setFragments(1);
    }

    @Override
    public void showHome() {

    }

    @Override
    public void showLogin() {
        fragmentChangeManager.setFragments(1);
    }

    @Override
    public void showServerSettings() {
        fragmentChangeManager.setFragments(0);
    }
}
