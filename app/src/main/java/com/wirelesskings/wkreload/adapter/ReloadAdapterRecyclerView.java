package com.wirelesskings.wkreload.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.model.ReloadItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ReloadAdapterRecyclerView extends RecyclerView.Adapter<ReloadAdapterRecyclerView.ViewHolder> {

    private List<ReloadItem> reloadItems;

    public interface Listened {
        void onClickItem(String id);
    }

    private Listened listened;


    public ReloadAdapterRecyclerView(Listened listened) {
        this.reloadItems = new ArrayList<>();
        this.listened = listened;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reload_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReloadItem reloadItem = reloadItems.get(position);
        holder.client_name.setText(reloadItem.getClientName());
        holder.client_number.setText(reloadItem.getClientNumber());
        holder.amount.setText(String.valueOf(reloadItem.getAmount()));
        holder.count.setText("x" + String.valueOf(reloadItem.getCount()));

        holder.date.setText(DateUtils.formatDateTime(holder.itemView.getContext(), reloadItem.getDate().getTime(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME));
        if (reloadItem.getStatus() != null)
            switch (reloadItem.getStatus()) {
                case DENIED:
                    holder.status.setImageResource(R.drawable.ic_status_error);
                    break;
                case INPROGRESS:
                    holder.status.setImageResource(R.drawable.ic_status_in_progress);
                    break;
                case SUCCESS:
                    holder.status.setImageResource(R.drawable.ic_status_done);
                    break;
            }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listened != null)
                    listened.onClickItem(reloadItem.getId());
            }
        });

        holder.seller.setText(reloadItem.getSeller());
    }

    @Override
    public int getItemCount() {
        return reloadItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView client_name;

        public View root;

        public TextView client_number;

        public TextView seller;

        public TextView amount;

        public TextView count;

        public TextView date;

        public ImageView status;

        public ViewHolder(View itemView) {
            super(itemView);
            client_name = itemView.findViewById(R.id.tv_name);
            root = itemView.findViewById(R.id.root);
            client_number = itemView.findViewById(R.id.tv_number);
            seller = itemView.findViewById(R.id.tv_seller);
            amount = itemView.findViewById(R.id.tv_amount);
            count = itemView.findViewById(R.id.tv_count);
            date = itemView.findViewById(R.id.tv_date);
            status = itemView.findViewById(R.id.iv_status);
        }
    }

    public void inserted(List<ReloadItem> items) {
        reloadItems.clear();
        for (ReloadItem reloadItem :
                items) {
            add(reloadItem);
        }
        notifyDataSetChanged();
    }

    private void add(ReloadItem reloadItem) {
        reloadItems.add(reloadItem);
    }

    private void change(ReloadItem reloadItem) {
        int i = reloadItems.indexOf(reloadItem);
        if (i != -1) {
            reloadItems.remove(reloadItem);
            reloadItems.add(i, reloadItem);
            notifyItemChanged(i);
        }
    }

    private void deleted(ReloadItem reloadItem) {
        int i = reloadItems.indexOf(reloadItem);
        if (i != -1) {
            reloadItems.remove(reloadItem);
            notifyItemRemoved(i);
        }
    }

    public void changed(List<ReloadItem> items) {
        for (ReloadItem reloadItem :
                items) {
            change(reloadItem);
        }
    }

    public void deleted(List<ReloadItem> items) {
        for (ReloadItem reloadItem :
                items) {
            deleted(reloadItem);
        }
    }


}
