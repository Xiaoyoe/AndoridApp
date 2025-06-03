package com.example.ticketmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketmall.R;
import com.example.ticketmall.entity.Ticket;

import java.util.ArrayList;
import java.util.List;

public class SingleItemAdapter extends RecyclerView.Adapter<SingleItemAdapter.ViewHolder> {

    private final List<Ticket> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket item = list.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvScore.setText(item.getScore());
        holder.tvContent1.setText(item.getContent1());
        holder.tvContent2.setText(item.getContent2());
        holder.tvPrice.setText(String.format("￥%.2f", item.getPrice()));

        // 修改图片加载逻辑
        int imageResId = item.getImageResId(holder.itemView.getContext());
        holder.ivCover.setImageResource(imageResId != 0 ? imageResId : R.drawable.error);

        holder.ivAdd.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onAddClick(item);
            }
        });

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    public void setList(List<Ticket> list) {
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
        void onAddClick(Ticket item);
        void onItemClick(Ticket item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle, tvScore, tvContent1, tvContent2, tvPrice;
        private final ImageView ivCover, ivAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvScore = itemView.findViewById(R.id.tv_score);
            tvContent1 = itemView.findViewById(R.id.tv_content1);
            tvContent2 = itemView.findViewById(R.id.tv_content2);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ivCover = itemView.findViewById(R.id.iv_cover);
            ivAdd = itemView.findViewById(R.id.iv_add);
        }
    }
}