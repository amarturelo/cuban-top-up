package com.wirelesskings.wkreload.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wirelesskings.wkreload.model.PromotionItemModel;

import java.util.List;

public class PromotionsListSpinnerAdapter extends ArrayAdapter<PromotionItemModel> implements ThemedSpinnerAdapter {
    private final ThemedSpinnerAdapter.Helper mDropDownHelper;

    public PromotionsListSpinnerAdapter(Context context, List<PromotionItemModel> objects) {
        super(context, android.R.layout.simple_list_item_2, objects);
        mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView == null) {
            // Inflate the drop down using the helper's LayoutInflater
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            view = convertView;
        }


        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(DateUtils.formatDateTime(
                parent.getContext()
                , getItem(position).getStartDate().getTime()
                , DateUtils.FORMAT_SHOW_DATE));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            // Inflate the drop down using the helper's LayoutInflater
            LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
            view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        } else {
            view = convertView;
        }

        TextView textView = view.findViewById(android.R.id.text1);
        TextView textView2 = view.findViewById(android.R.id.text2);
        textView.setText(getItem(position).getTitle());
        textView2.setText(DateUtils.formatDateTime(
                parent.getContext()
                , getItem(position).getStartDate().getTime()
                , DateUtils.FORMAT_SHOW_DATE));
        return view;
    }

    @Override
    public Resources.Theme getDropDownViewTheme() {
        return mDropDownHelper.getDropDownViewTheme();
    }

    @Override
    public void setDropDownViewTheme(Resources.Theme theme) {
        mDropDownHelper.setDropDownViewTheme(theme);
    }
}