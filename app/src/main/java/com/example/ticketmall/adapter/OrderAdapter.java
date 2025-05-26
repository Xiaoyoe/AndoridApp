package com.example.ticketmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketmall.R;
import com.example.ticketmall.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order item = list.get(position);

        holder.tvOrder.setText(String.format("订单号：%d", item.getId()));
        holder.tvTime.setText(String.format("下单时间：%s", item.getCreateTime()));
        holder.tvPrice.setText(String.format("￥%.2f", item.getTotal()));
        holder.tvDetail.setText(String.format("详情：%s", item.getDetail()));
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDeleteClick(holder.getAdapterPosition(), item);
                }
            }
        });
    }

    public void setList(List<Order> list) {
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
        void onDeleteClick(int position, Order item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvOrder, tvDelete, tvTime, tvPrice, tvDetail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrder = itemView.findViewById(R.id.tv_order);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDetail = itemView.findViewById(R.id.tv_detail);
        }
    }
}
