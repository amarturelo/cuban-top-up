package com.wirelesskings.wkreload.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.model.PreReloadItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 18/10/2017.
 */

public class PreReloadAdapterRecyclerView extends RecyclerView.Adapter<PreReloadAdapterRecyclerView.ViewHolder> {

    private List<PreReloadItemModel> preReloadItemModels;

    public List<PreReloadItemModel> getItems() {
        return preReloadItemModels;
    }

    public interface Listened {
        void onClickItem(View view, int id);
    }

    private Listened mListened;


    public PreReloadAdapterRecyclerView(Listened listened) {
        this.preReloadItemModels = new ArrayList<>();
        this.mListened = listened;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_reload_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PreReloadItemModel reloadItem = preReloadItemModels.get(position);
        holder.client_name.setText(reloadItem.getClientName());
        holder.client_number.setText(reloadItem.getClientNumber());
        holder.amount.setText(String.valueOf(reloadItem.getAmount()));
        holder.count.setText("x" + String.valueOf(reloadItem.getCount()));

    }

    @Override
    public int getItemCount() {
        return preReloadItemModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView client_name;

        public View root;

        public TextView client_number;


        public TextView amount;

        public TextView count;


        public ViewHolder(View itemView) {
            super(itemView);
            client_name = itemView.findViewById(R.id.tv_name);
            root = itemView.findViewById(R.id.root);
            client_number = itemView.findViewById(R.id.tv_number);
            amount = itemView.findViewById(R.id.tv_amount);
            count = itemView.findViewById(R.id.tv_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListened != null) {
                        mListened.onClickItem(v,getAdapterPosition());
                    }
                }
            });
        }
    }

    public void add(PreReloadItemModel reloadItem) {
        preReloadItemModels.add(reloadItem);
        notifyItemInserted(preReloadItemModels.size() - 1);
    }

    public void deleted(int pos) {
        preReloadItemModels.remove(pos);
        notifyItemRemoved(pos);
    }
}
