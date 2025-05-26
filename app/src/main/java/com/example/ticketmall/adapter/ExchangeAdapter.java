package com.example.ticketmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketmall.R;
import com.example.ticketmall.entity.Exchange;

import java.util.ArrayList;
import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ViewHolder> {

    private List<Exchange> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ExchangeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exchange, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeAdapter.ViewHolder holder, int position) {
        Exchange item = list.get(position);
        holder.tvExchangeId.setText(String.format("兑换记录：%d", item.getId()));
        holder.tvTime.setText(String.format("兑换时间：%s", item.getCreateTime()));
        holder.tvPoints.setText(String.valueOf(item.getPoints()));
        holder.tvStuffName.setText(String.format("商品：%s", item.getStuffName()));
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDeleteClick(holder.getAdapterPosition(), item);
                }
            }
        });
    }

    public void setList(List<Exchange> list) {
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

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position, Exchange item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvExchangeId, tvDelete, tvTime, tvPoints, tvStuffName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExchangeId = itemView.findViewById(R.id.tv_exchange_id);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPoints = itemView.findViewById(R.id.tv_points);
            tvStuffName = itemView.findViewById(R.id.tv_stuff_name);
        }
    }
} 