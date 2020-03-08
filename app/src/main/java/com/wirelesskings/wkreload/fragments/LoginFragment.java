package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.domain.model.internal.Credentials;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;
import com.wirelesskings.wkreload.navigation.Navigator;


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

    private WK wk;


    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wk = WK.getInstance();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mUser = view.findViewById(R.id.et_user);
        mPass = view.findViewById(R.id.et_pass);
        mToken = view.findViewById(R.id.et_token);
        mUserNauta = view.findViewById(R.id.et_user_nauta);
        mNautaPass = view.findViewById(R.id.et_pass_nauta);

        buttom = view.findViewById(R.id.login_bottom);
        buttom.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WKSDK wksdk = wk.getWKSessionDefault();

        if (wksdk != null) {
            mUser.setText(wk.getCredentials().getCredentials().getUsername());
            mToken.setText(wk.getCredentials().getCredentials().getToken());
            mUserNauta.setText(getUserNauta(wk.getCredentials().getEmail()));
            mNautaPass.setText((wk.getCredentials().getPassword()));
        }
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

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        /*args.putString(ARGS_NAUTA_USER, email);
        args.putString(ARGS_NAUTA_PASS, pass);
        args.putString(ARGS_WK_USER, wk_user);
        args.putString(ARGS_WK_TOKEN, wk_token);*/
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getUserNauta(String email) {
        if (email.contains("@"))
            return email.split("@")[0];
        return email;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bottom:
                //Navigator.goToReload(getContext());
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

            WKSDK wksdk = wk.getWKSessionDefault();

            if (wksdk != null && wksdk.getServerConfig().equals(serverConfig) && wksdk.getServerConfig().isActive())
                onLoginFragmentListener.onGoToMain();
            else
                onLoginFragmentListener.onLogin(serverConfig);
        }
    }

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
        void onLogin(ServerConfig serverConfig);

        void onGoToMain();
    }
}
