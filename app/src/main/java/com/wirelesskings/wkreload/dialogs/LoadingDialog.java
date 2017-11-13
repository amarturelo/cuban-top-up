package com.wirelesskings.wkreload.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.wkreload.R;

/**
 * Created by Alberto on 01/11/2017.
 */

public class LoadingDialog {

    private BottomDialog bottomDialog;
    private View v;
    private View btnCancel;

    public interface LoadingListener {
        void onCancel();
    }

    private LoadingListener listener;

    public LoadingDialog(Context context) {
        v = LayoutInflater.from(context).inflate(R.layout.layout_view_loading, null);

        bottomDialog = new BottomDialog.Builder(context)
                .setTitle(R.string.waiting)
                .setContent(R.string.loading_content)
                .setCancelable(false)
                .setCustomView(v)
                .build();

        btnCancel = v.findViewById(R.id.btn_cancel);
    }

    public void show(final LoadingListener listener) {
        bottomDialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                bottomDialog.dismiss();
            }
        });
    }

    public void dismiss() {
        bottomDialog.dismiss();
    }
}
