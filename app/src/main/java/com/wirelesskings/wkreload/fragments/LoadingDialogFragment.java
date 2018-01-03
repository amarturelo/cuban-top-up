package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wirelesskings.wkreload.R;

/**
 * Created by alberto on 1/01/18.
 */

public class LoadingDialogFragment extends BottomSheetDialogFragment {
    // TODO: Customize parameter argument names
    private static final String ARG_PROCESS_POS = "process_pos";
    private Listener mListener;

    private View btnCancel;

    public interface Listener {
        void onCancel(int pos);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_view_loading, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        /*if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCancel = view.findViewById(R.id.btn_cancel);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    // TODO: Customize parameters
    public static LoadingDialogFragment newInstance() {
        final LoadingDialogFragment fragment = new LoadingDialogFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
