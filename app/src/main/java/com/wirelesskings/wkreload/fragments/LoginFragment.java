package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.activities.LoginActivity;
import com.wirelesskings.wkreload.activities.MainActivity;
import com.wirelesskings.wkreload.domain.model.internal.Credentials;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    public static final String ARGS_NAUTA_USER = "args_username";
    public static final String ARGS_NAUTA_PASS = "args_pass";

    private EditText mUser;
    private EditText mPass;
    private EditText mToken;
    private View mBackToSettings;

    private View buttom;

    private ServerConfig serverConfig;


    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        serverConfig = new ServerConfig();

        serverConfig.setEmail(getUserNauta())
                .setPassword(getPassNauta());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mUser = view.findViewById(R.id.et_user);
        mPass = view.findViewById(R.id.et_pass);
        mToken = view.findViewById(R.id.et_token);
        buttom = view.findViewById(R.id.login_bottom);
        mBackToSettings = view.findViewById(R.id.back_to_settings);
        mBackToSettings.setOnClickListener(this);
        buttom.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentListener)
            onLoginFragmentListener = (OnLoginFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onLoginFragmentListener = null;
    }

    public static LoginFragment newInstance(String email, String pass) {
        Bundle args = new Bundle();
        args.putString(ARGS_NAUTA_USER, email);
        args.putString(ARGS_NAUTA_PASS, pass);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getUserNauta() {
        return getArguments().getString(ARGS_NAUTA_USER);
    }

    public String getPassNauta() {
        return getArguments().getString(ARGS_NAUTA_PASS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bottom:
                clickLogin();
                break;
            case R.id.back_to_settings:
                backToSettings();
                break;
        }
    }

    private void backToSettings() {
        if(onLoginFragmentListener!=null)
            onLoginFragmentListener.onBackSettings();
    }

    private void clickLogin() {
        if (check()) {

            String crypto = null;
            try {
                crypto = Crypto.hashPassword(mPass.getText().toString(),
                        mToken.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            serverConfig.setCredentials(
                    new Credentials()
                            .setToken(Crypto.md5(mToken.getText().toString()))
                            .setUsername(mUser.getText().toString())
                            .setPassword(crypto)
            );
            onLoginFragmentListener.onLoginCallback(serverConfig);
        }
    }

    //TODO hace validacion
    private boolean check() {
        boolean check = true;

        if (mUser.getText().toString().trim().isEmpty()) {
            mUser.setError("Debe espesificar su usuario");
            check = false;
        }
        if (mPass.getText().toString().trim().isEmpty()) {
            mPass.setError("Debe espesificar su contraseña");
            check = false;
        }
        if (mToken.getText().toString().trim().isEmpty()) {
            mToken.setError("Debe espesificar su token de accesso");
            check = false;
        }

        return check;
    }

    private OnLoginFragmentListener onLoginFragmentListener;



    public interface OnLoginFragmentListener {
        void onLoginCallback(ServerConfig serverConfig);

        void onBackSettings();
    }
}
