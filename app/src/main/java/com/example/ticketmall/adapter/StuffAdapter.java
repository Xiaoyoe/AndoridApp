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

        // 根据 imageUrl 和 imageResId 的情况加载图片
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            // 优先加载网络图片
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_cart_add)
                    .error(R.drawable.ic_cart_cut)
                    .into(holder.ivImage);
        } else if (item.getImageResId() != null) {
            // 加载本地资源图片
            holder.ivImage.setImageResource(item.getImageResId());
        } else {
            // 显示默认占位图
            holder.ivImage.setImageResource(R.drawable.ic_cart_add);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onExchangeClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stuffList.size();
    }

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