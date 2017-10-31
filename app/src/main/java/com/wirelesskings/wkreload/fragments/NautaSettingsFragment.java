package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wirelesskings.wkreload.R;

public class NautaSettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public EditText mUserNauta;
    public EditText mNautaPass;


    public NautaSettingsFragment() {
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
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static NautaSettingsFragment newInstance() {

        Bundle args = new Bundle();

        NautaSettingsFragment fragment = new NautaSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFragmentNautaSettingsListened {
        void onNautaSettings(String email, String password);
    }
}
