package com.wirelesskings.wkreload.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wirelesskings.data.model.mapper.ServerConfigDataMapper;
import com.wirelesskings.data.repositories.RealmServerConfigRepository;
import com.wirelesskings.data.repositories.ServerRepositoryImpl;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.domain.interactors.ServerConfigInteractor;
import com.wirelesskings.wkreload.domain.interactors.ServerInteractor;
import com.wirelesskings.wkreload.domain.model.internal.Credentials;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.executor.JobExecutor;
import com.wirelesskings.wkreload.fragments.LoginFragment;
import com.wirelesskings.wkreload.fragments.NautaSettingsFragment;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.ResultListener;
import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Constants;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.LinkedHashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private LoginPresenter loginPresenter;

    private ServerConfig serverConfig;

    private View buttom;
    private TextView tvLoginBottom;

    private int mode = 0;

    private NautaSettingsFragment nautaSettingsFragment;
    private LoginFragment loginFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nautaSettingsFragment = NautaSettingsFragment.newInstance();
        loginFragment = LoginFragment.newInstance();

        loginPresenter = new LoginPresenter(JobExecutor.getInstance(),
                new ServerConfigInteractor(
                        new RealmServerConfigRepository(
                                new ServerConfigDataMapper())));
        initComponents();
    }

    private void initComponents() {
        buttom = findViewById(R.id.login_bottom);
        tvLoginBottom = (TextView) findViewById(R.id.tv_login_bottom);
        buttom.setOnClickListener(v -> {
            if (mode == 0) {
                setNautaSettings(nautaSettingsFragment.mUserNauta.getText().toString(), nautaSettingsFragment.mNautaPass.getText().toString());
            } else {
                doLoginSettings(loginFragment.mUser.getText().toString(), loginFragment.mPass.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.bindView(this);
    }

    @Override
    public void showServerConfig(ServerConfig serverConfig) {
        if (serverConfig.isEmpty())
            showConfig();
        else {
            showLogin();
        }
    }

    private void showConfig() {
        mode = 0;
        tvLoginBottom.setText(R.string.next);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, nautaSettingsFragment).commit();
    }

    private void showLogin() {
        mode = 1;
        tvLoginBottom.setText(R.string.login);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, loginFragment).commit();
    }

    public void setNautaSettings(String email, String password) {
        if (serverConfig == null)
            serverConfig = new ServerConfig();
        serverConfig.setEmail(email)
                .setPassword(password);
        showLogin();
    }

    public void doLoginSettings(String email, String password) {
        if (serverConfig.getCredentials() != null && serverConfig.getCredentials().getStatus() == 1) {
            if (serverConfig.getCredentials().getUsername().equals(email) && serverConfig.getCredentials().getPassword().equals(password)) {
                loginComplete();
            }
        } else {
            Setting out = new Setting("amarturelo@nauta.cu", "adriana*2017");
            out.setServerType(Constants.SMTP_PLAIN); //0 for plain , 1 for ssl
            out.setHost("smtp.nauta.cu");
            out.setPort(Constants.SMTP_PLAIN_PORT); //25 for smtp plain,465 for smtp ssl.


            Setting in = new Setting("amarturelo@nauta.cu", "adriana*2017");
            in.setServerType(Constants.IMAP_PLAIN);
            in.setHost("imap.nauta.cu");
            in.setPort(Constants.IMAP_PLAIN_PORT);
            String salt = "U33756-AMARTURELO";
            String crypto = null;
            try {
                crypto = Crypto.hashPassword(password, salt);
            } catch (Exception e) {
                e.printStackTrace();
            }

            loginPresenter.setServerInteractor(new ServerInteractor(
                    new ServerRepositoryImpl(
                            new Middleware(
                                    in, out, Crypto.md5(salt)
                            )
                    )
            ));
            serverConfig.setCredentials(new Credentials().setUsername(email).setPassword(password));

            Middleware.init(in, out, Crypto.md5(salt));
            Middleware middleware = Middleware.getInstance();


            Map<String, Object> params = new LinkedHashMap<>();
            params.put("user", serverConfig.getCredentials().getUsername());
            params.put("pass", crypto);
            params.put("user_nauta", serverConfig.getEmail());

            middleware.call("update", params, new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    Log.d(LoginActivity.class.getSimpleName(), result);
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.d(LoginActivity.class.getSimpleName(), error);
                }
            });
            //loginPresenter.login(serverConfig.getEmail(), serverConfig.getPassword(), serverConfig.getCredentials().getUsername(), crypto);
        }

    }

    @Override
    public void loginComplete() {
        finish();
        goToMain();
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
