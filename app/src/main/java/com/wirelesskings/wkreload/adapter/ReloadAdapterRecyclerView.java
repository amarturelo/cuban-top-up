package com.wirelesskings.wkreload.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.model.ReloadItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ReloadAdapterRecyclerView extends RecyclerView.Adapter<ReloadAdapterRecyclerView.ViewHolder> {

    private List<ReloadItem> reloadItems;

    public ReloadAdapterRecyclerView(List<ReloadItem> reloadItems) {
        this.reloadItems = reloadItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reload_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReloadItem reloadItem = reloadItems.get(position);
        holder.client_name.setText(reloadItem.getClientName());
        holder.client_number.setText(reloadItem.getClientNumber());
        holder.amount.setText(String.valueOf(reloadItem.getAmmount()));
        holder.count.setText("x"+String.valueOf(reloadItem.getCount()));
        holder.seller.setText(reloadItem.getSeller());
    }

    @Override
    public int getItemCount() {
        return reloadItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView client_name;

        @BindView(R.id.tv_number)
        TextView client_number;

        @BindView(R.id.tv_seller)
        TextView seller;

        @BindView(R.id.tv_amount)
        TextView amount;

        @BindView(R.id.tv_count)
        TextView count;

        @BindView(R.id.tv_date)
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
