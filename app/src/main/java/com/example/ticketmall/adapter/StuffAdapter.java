package com.example.ticketmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketmall.R;
import com.example.ticketmall.entity.Stuff;

import java.util.ArrayList;
import java.util.List;

public class StuffAdapter extends RecyclerView.Adapter<StuffAdapter.ViewHolder> {

    private final List<Stuff> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public StuffAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stuff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StuffAdapter.ViewHolder holder, int position) {
        Stuff item = list.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPoints.setText(item.getPoints() + "积分");
        if (item.getImageResId()!=null){
            holder.ivCover.setImageResource(item.getImageResId());
        }
        holder.tvExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onExchangeClick(item);
                }
            }
        });
    }

    public void setList(List<Stuff> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onExchangeClick(Stuff item);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName, tvPoints, tvExchange;

        private final ImageView ivCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPoints = itemView.findViewById(R.id.tv_points);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvExchange = itemView.findViewById(R.id.tv_exchange);
        }
    }
}
