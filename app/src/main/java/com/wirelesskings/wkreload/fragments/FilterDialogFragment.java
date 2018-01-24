package com.wirelesskings.wkreload.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WK;
import com.wirelesskings.wkreload.domain.filter.ClientNameFilter;
import com.wirelesskings.wkreload.domain.filter.ClientNumberFilter;
import com.wirelesskings.wkreload.domain.filter.DateFilter;
import com.wirelesskings.wkreload.domain.filter.Filter;
import com.wirelesskings.wkreload.domain.filter.ReloadStateFilter;
import com.wirelesskings.wkreload.domain.filter.SellerNameFilter;
import com.wirelesskings.wkreload.model.FilterItemModel;
import com.wirelesskings.wkreload.navigation.Navigator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FilterDialogFragment extends BottomSheetDialogFragment implements FilterDialogContract.View, View.OnClickListener {

    private FilterDialogPresenter presenter;

    private SearchableSpinner spinnerClientName;
    private SearchableSpinner spinnerClientNumber;
    private SearchableSpinner spinnerSellerName;
    private Spinner spinnerReloadState;

    private Button pickDate;

    private View filter;

    private Filter filterClientName;
    private Filter filterClientNumber;
    private Filter filterSellerName;
    private Filter filterReloadState;
    private Filter filterDateRange;

    // TODO: Customize parameters
    public static FilterDialogFragment newInstance() {
        final FilterDialogFragment fragment = new FilterDialogFragment();
        final Bundle args = new Bundle();
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
        spinnerClientName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    filterClientName = null;
                else
                    filterClientName = new ClientNameFilter()
                            .setName(((FilterItemModel) parent.getItemAtPosition(position)).getText());
                Log.d(FilterDialogFragment.class.getSimpleName(), ((FilterItemModel) parent.getItemAtPosition(position)).getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerClientNumber = view.findViewById(R.id.spinner_client_number);
        spinnerClientNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    filterClientNumber = null;
                else
                    filterClientNumber = new ClientNumberFilter()
                            .setClientNumber(((FilterItemModel) parent.getItemAtPosition(position)).getText());
                Log.d(FilterDialogFragment.class.getSimpleName(), ((FilterItemModel) parent.getItemAtPosition(position)).getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerReloadState = view.findViewById(R.id.spinner_state);
        spinnerReloadState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    filterReloadState = null;
                else
                    filterReloadState = new ReloadStateFilter()
                            .setState((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerSellerName = view.findViewById(R.id.spinner_seller_name);
        spinnerSellerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    filterSellerName = null;
                else
                    filterSellerName = new SellerNameFilter()
                            .setName(((FilterItemModel) parent.getItemAtPosition(position)).getText());
                Log.d(FilterDialogFragment.class.getSimpleName(), ((FilterItemModel) parent.getItemAtPosition(position)).getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pickDate = view.findViewById(R.id.btn_pick_date);
        filter = view.findViewById(R.id.btn_ok);
        pickDate.setOnClickListener(this);
        filter.setOnClickListener(this);
        presenter.bindView(this);
        presenter.onClients();
        presenter.onSellerName();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
    }

    @Override
    public void onDetach() {
        presenter.release();
        super.onDetach();
    }

    @Override
    public void renderClientName(List<FilterItemModel> clientName) {
        clientName.add(0, new FilterItemModel()
                .setId("0")
                .setText("TODOS"));
        ArrayAdapter<FilterItemModel> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, clientName);

        spinnerClientName.setAdapter(adapter);
    }

    @Override
    public void renderClientNumbers(List<FilterItemModel> clientNumber) {
        clientNumber.add(0, new FilterItemModel()
                .setId("0")
                .setText("TODOS"));
        ArrayAdapter<FilterItemModel> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, clientNumber);

        spinnerClientNumber.setAdapter(adapter);
    }

    @Override
    public void renderSellerName(List<FilterItemModel> sellerName) {
        sellerName.add(0, new FilterItemModel()
                .setId("0")
                .setText("TODOS"));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pick_date:
                showPickDate();
                break;
            case R.id.btn_ok:
                doFilter();
                break;
        }
    }

    private void doFilter() {
        ArrayList<Filter> filters = new ArrayList<>();
        if (filterClientName != null)
            filters.add(filterClientName);
        if (filterClientNumber != null)
            filters.add(filterClientNumber);
        if (filterDateRange != null)
            filters.add(filterDateRange);
        if (filterReloadState != null)
            filters.add(filterReloadState);
        if (filterSellerName != null)
            filters.add(filterSellerName);


        Navigator.goToSearchActivity(getActivity(), filters);
    }

    private void showPickDate() {
        DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(new DateRangePickerFragment.OnDateRangeSelectedListener() {
            @Override
            public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
                Calendar start = Calendar.getInstance();
                start.set(startYear, startMonth, startDay);

                Calendar end = Calendar.getInstance();
                end.set(endYear, endMonth, endDay);

                filterDateRange = new DateFilter()
                        .setStart(start.getTime())
                        .setEnd(end.getTime());

                pickDate.setText(filterDateRange.toString());
            }
        }, false);

        dateRangePickerFragment.show(getFragmentManager(), DateRangePickerFragment.class.getSimpleName());
    }


}
