package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wirelesskings.wkreload.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    public static final String ARGS_USERNAME = "args_username";

    private OnFragmentLoginListened mListener;

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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NautaSettingsFragment.OnFragmentNautaSettingsListened) {
            mListener = (OnFragmentLoginListened) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    public static LoginFragment newInstance(String email) {
        Bundle args = new Bundle();
        args.putString(ARGS_USERNAME, email);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public interface OnFragmentLoginListened {
        void onLoginSettings(String email, String password);
    }

    private String getArgsUsername() {
        return getArguments().getString(ARGS_USERNAME);
    }
}
