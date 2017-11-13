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
import android.widget.TextView;

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
    public static final String ARGS_WK_USER = "args_user";
    public static final String ARGS_WK_TOKEN = "args_token";

    private EditText mUser;
    private EditText mPass;
    private EditText mToken;

    private EditText mUserNauta;
    private EditText mNautaPass;
    private TextView msTub;

    private View buttom;


    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mUser = view.findViewById(R.id.et_user);
        mUser.setText(getWKUser());
        mPass = view.findViewById(R.id.et_pass);
        mToken = view.findViewById(R.id.et_token);
        mToken.setText(getWKToken());
        mUserNauta = view.findViewById(R.id.et_user_nauta);
        mUserNauta.setText(getUserNauta());
        mNautaPass = view.findViewById(R.id.et_pass_nauta);
        mNautaPass.setText(getPassNauta());

        buttom = view.findViewById(R.id.login_bottom);
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

    public static LoginFragment newInstance(String email, String pass, String wk_user, String wk_token) {
        Bundle args = new Bundle();
        args.putString(ARGS_NAUTA_USER, email);
        args.putString(ARGS_NAUTA_PASS, pass);
        args.putString(ARGS_WK_USER, wk_user);
        args.putString(ARGS_WK_TOKEN, wk_token);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getUserNauta() {
        String email = getArguments().getString(ARGS_NAUTA_USER, "");
        if (email.contains("@"))
            return email.split("@")[0];
        return email;
    }

    public String getPassNauta() {
        return getArguments().getString(ARGS_NAUTA_PASS, "");
    }

    public String getWKUser() {
        return getArguments().getString(ARGS_WK_USER, "");
    }

    public String getWKToken() {
        return getArguments().getString(ARGS_WK_TOKEN, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bottom:
                clickLogin();
                break;
        }
    }


    private void clickLogin() {
        if (check()) {

            ServerConfig serverConfig = new ServerConfig();

            String crypto = null;
            try {
                crypto = Crypto.hashPassword(mPass.getText().toString(),
                        mToken.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            serverConfig
                    .setEmail(mUserNauta.getText().toString() + "@nauta.cu")
                    .setPassword(mNautaPass.getText().toString())
                    .setCredentials(
                            new Credentials()
                                    .setToken(mToken.getText().toString())
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
        if (mUserNauta.getText().toString().isEmpty()) {
            mUserNauta.setError("El correo nauna no puede estar vacio");
            check = false;
        } else if (mUserNauta.getText().toString().contains("@")) {
            mUserNauta.setError("No es necesario poner @nauta.cu, nosotros lo hacemos por ti");
            check = false;
        }
        if (mNautaPass.getText().toString().isEmpty()) {
            mNautaPass.setError("Debe espesificar su contraseña nauta");
            check = false;
        }

        return check;
    }

    private OnLoginFragmentListener onLoginFragmentListener;


    public interface OnLoginFragmentListener {
        void onLoginCallback(ServerConfig serverConfig);

    }
}
