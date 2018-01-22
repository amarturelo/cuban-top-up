package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.model.FilterItemModel;

import java.util.List;


public class FilterDialogFragment extends BottomSheetDialogFragment implements FilterDialogContract.View {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private Listener mListener;

    private FilterDialogPresenter presenter;

    private Spinner spinnerClientName;
    private Spinner spinnerClientNumber;
    private Spinner spinnerSellerName;
    private Spinner spinnerReloadState;

    // TODO: Customize parameters
    public static FilterDialogFragment newInstance(int itemCount) {
        final FilterDialogFragment fragment = new FilterDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new FilterDialogPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        spinnerClientName = view.findViewById(R.id.spinner_client_name);
        spinnerClientNumber = view.findViewById(R.id.spinner_client_number);
        spinnerReloadState = view.findViewById(R.id.spinner_state);
        spinnerSellerName = view.findViewById(R.id.spinner_seller_name);
        presenter.bindView(this);
        presenter.onClients(WK.getInstance().getCredentials().getCredentials().getUsername());
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

    @Override
    public void renderClientName(List<FilterItemModel> clientName) {
        ArrayAdapter<FilterItemModel> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, clientName);

        spinnerClientName.setAdapter(adapter);
    }

    @Override
    public void renderClientNumbers(List<FilterItemModel> clientNumber) {
        ArrayAdapter<FilterItemModel> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, clientNumber);

        spinnerClientNumber.setAdapter(adapter);
    }

    @Override
    public void renderSellerName(List<FilterItemModel> sellerName) {
        ArrayAdapter<FilterItemModel> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sellerName);

        spinnerSellerName.setAdapter(adapter);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void showLoading() {

    }

    public interface Listener {
        void onItemClicked(int position);
    }


}
