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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        mNautaPass = view.findViewById(R.id.et_pass_nauta);
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

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

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

    public interface OnFragmentSettingsListened {
        void onSettingsCallback(String email, String password);
    }
}
