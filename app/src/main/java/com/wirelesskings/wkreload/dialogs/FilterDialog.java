package com.wirelesskings.wkreload.dialogs;

import android.content.Context;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.wkreload.R;

/**
 * Created by Alberto on 29/10/2017.
 */

public class FilterDialog {
    private BottomDialog bottomDialog;

    public FilterDialog(Context context) {
        bottomDialog = new BottomDialog.Builder(context)
                .setTitle(R.string.title_filter)
                .build();
    }
}
