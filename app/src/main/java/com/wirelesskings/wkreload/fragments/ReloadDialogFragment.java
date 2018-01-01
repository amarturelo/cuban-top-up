package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.wirelesskings.wkreload.R;

/**
 * Created by alberto on 1/01/18.
 */

public class ReloadDialogFragment extends BottomSheetDialogFragment {
    private Button buttonOk;

    private TextView clientName;

    private TextView clientNumber;

    private Spinner spAmount;

    private Spinner spCount;

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private Listener mListener;

    // TODO: Customize parameters
    public static ReloadDialogFragment newInstance(int itemCount) {
        final ReloadDialogFragment fragment = new ReloadDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reload_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        buttonOk = view.findViewById(R.id.btn_ok);
        clientName = view.findViewById(R.id.client_name);
        clientNumber = view.findViewById(R.id.client_number);
        spAmount = view.findViewById(R.id.sp_amount);
        spCount = view.findViewById(R.id.sp_count);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onReload(int position);
    }
}
