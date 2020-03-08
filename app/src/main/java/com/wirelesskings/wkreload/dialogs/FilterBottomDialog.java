package com.wirelesskings.wkreload.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.wkreload.R;

/**
 * Created by Alberto on 29/10/2017.
 */

public class FilterBottomDialog {
    private BottomDialog bottomDialog;
    private View v;

    public interface FilterDialogListener {
        void onFilter();

        void onCancel();
    }

    public FilterBottomDialog(Context context) {
        v = LayoutInflater.from(context).inflate(R.layout.layout_view_filter, null);

        bottomDialog = new BottomDialog.Builder(context)
                .setTitle(R.string.title_filter)
                .setCustomView(v)
                .build();
    }

    public void show() {
        bottomDialog.show();


    }
}
