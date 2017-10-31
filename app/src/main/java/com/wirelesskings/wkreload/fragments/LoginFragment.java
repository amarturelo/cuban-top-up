package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wirelesskings.wkreload.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    public static final String ARGS_USERNAME = "args_username";

    public EditText mUser;
    public EditText mPass;


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
        mPass = view.findViewById(R.id.et_pass);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        //args.putString(ARGS_USERNAME, email);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
