package com.example.ticketmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ticketmall.R;
import com.example.ticketmall.entity.Stuff;

import java.util.ArrayList;
import java.util.List;

public class StuffAdapter extends RecyclerView.Adapter<StuffAdapter.StuffViewHolder> {

    private List<Stuff> stuffList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Context context;

    public StuffAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Stuff> stuffList) {
        this.stuffList = stuffList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StuffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stuff, parent, false);
        return new StuffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StuffViewHolder holder, int position) {
        Stuff item = stuffList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPoints.setText(String.valueOf(item.getPoints()));

        // 优化后的图片加载逻辑：优先本地资源 -> 网络图片 -> 默认占位图
        if (item.getImageResId() != null && !item.getImageResId().isEmpty()) {
            // 1. 先尝试加载本地资源
            int resourceId = context.getResources().getIdentifier(
                    item.getImageResId(),
                    "drawable",
                    context.getPackageName());

            if (resourceId != 0) {
                // 本地资源存在，直接加载
                holder.ivImage.setImageResource(resourceId);
            } else if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                // 2. 本地资源不存在但网络图片可用，加载网络图片
                Glide.with(context)
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.error) // 加载中显示占位图
                        .error(R.drawable.error)      // 加载失败显示错误图
                        .into(holder.ivImage);
            } else {
                // 3. 两者都不可用，显示默认占位图
                holder.ivImage.setImageResource(R.drawable.error);
            }
        } else if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            // 只有网络图片的情况
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.error)
                    .error(R.drawable.error)
                    .into(holder.ivImage);
        } else {
            // 没有任何图片资源，显示默认占位图
            holder.ivImage.setImageResource(R.drawable.error);
        }

        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onExchangeClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stuffList.size();
    }

    // ViewHolder 类保持不变
    public static class StuffViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPoints;
        ImageView ivImage;

        public StuffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPoints = itemView.findViewById(R.id.tv_points);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }

    public interface OnItemClickListener {
        void onExchangeClick(Stuff item);
    }
}