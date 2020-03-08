package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wirelesskings.wkreload.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARGS_NAUTA_USER = "args_username";
    public static final String ARGS_NAUTA_PASS = "args_pass";

    // TODO: Rename and change types of parameters
    private EditText mUserNauta;
    private EditText mNautaPass;
    private TextView msTub;
    private View mNextBottom;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nauta_settings, container, false);

        mUserNauta = view.findViewById(R.id.et_user_nauta);
        mUserNauta.setText(getUserNauta());
        mNautaPass = view.findViewById(R.id.et_pass_nauta);
        mNautaPass.setText(getPassNauta());

        mNextBottom = view.findViewById(R.id.next_bottom);
        msTub = view.findViewById(R.id.stub);
        mNextBottom.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSettingsListened)
            settingsListened = (OnFragmentSettingsListened) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        settingsListened = null;
    }

    public static SettingsFragment newInstance(String email, String pass) {

        Bundle args = new Bundle();

        args.putString(ARGS_NAUTA_USER, email);
        args.putString(ARGS_NAUTA_PASS, pass);

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private OnFragmentSettingsListened settingsListened;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_bottom:
                clickNext();
                break;
        }
    }

    private void clickNext() {
        if (check())
            settingsListened.onSettingsCallback(
                    mUserNauta.getText().toString() + msTub.getText().toString(),
                    mNautaPass.getText().toString());
    }

    private boolean check() {
        boolean check = true;

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

    public String getUserNauta() {
        String email = getArguments().getString(ARGS_NAUTA_USER, "");
        if (email.contains("@"))
            return email.split("@")[0];
        return email;
    }

    public String getPassNauta() {
        return getArguments().getString(ARGS_NAUTA_PASS, "");
    }

    public interface OnFragmentSettingsListened {
        void onSettingsCallback(String email, String password);
    }
}
