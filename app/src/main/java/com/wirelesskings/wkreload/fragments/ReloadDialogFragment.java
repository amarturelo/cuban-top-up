package com.wirelesskings.wkreload.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.WKSDK;
import com.wirelesskings.wkreload.model.FilterItemModel;
import com.wirelesskings.wkreload.model.PreReloadItemModel;

import java.util.List;

/**
 * Created by alberto on 1/01/18.
 */

public class ReloadDialogFragment extends BottomSheetDialogFragment implements ReloadContract.View {
    private Button buttonOk;

    private TextView clientName;

    private TextView clientNumber;

    private Spinner spAmount;

    private Spinner spCount;

    private ReloadPresenter presenter;

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private Listener mListener;

    public ReloadDialogFragment() {
        presenter = new ReloadPresenter();
    }

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
        presenter.bindView(this);

        presenter.onClients();
        buttonOk = view.findViewById(R.id.btn_add);
        clientName = view.findViewById(R.id.client_name);
        clientNumber = view.findViewById(R.id.client_number);
        spAmount = view.findViewById(R.id.sp_amount);
        spCount = view.findViewById(R.id.sp_count);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check() && mListener != null) {
                    if (clientNumbers != null)
                        if (clientNumbers.contains(clientNumber.getText().toString())) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Alerta")
                                    .setMessage("Este numero ha sido recargado anteriormente")
                                    .setPositiveButton("recargar",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    doReload();
                                                }
                                            })
                                    .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else
                            doReload();
                    else
                        doReload();
                }
            }
        });
    }

    private void doReload() {
        dismiss();

        mListener.onReload(new PreReloadItemModel()
                .setCount(Integer.parseInt(spCount.getSelectedItem().toString()))
                .setClientName(clientName.getText().toString().trim())
                .setClientNumber(clientNumber.getText().toString().trim())
                .setAmount(Integer.parseInt(spAmount.getSelectedItem().toString())));
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

    private boolean check() {
        boolean check = true;
        if (clientName.getText().toString().isEmpty()) {
            clientName.setError("Debe espesificar el nombre del cliente");
            check = false;
        }
        if (clientNumber.getText().toString().isEmpty()) {
            clientNumber.setError("Debe espesificar el númbero a recargar");
            check = false;
        }
        if (clientNumber.getText().length() != 8) {
            clientNumber.setError("El numero a recargar tiene que ser de 8 digitos");
            check = false;
        }


        return check;
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    private List<String> clientNumbers;

    @Override
    public void renderClientNumbers(List<String> clientNumber) {
        this.clientNumbers = clientNumber;
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
        void onReload(PreReloadItemModel preReloadItemModel);
    }
}
