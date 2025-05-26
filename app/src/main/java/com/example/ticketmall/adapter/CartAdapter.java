package com.example.ticketmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ticketmall.R;
import com.example.ticketmall.entity.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Cart> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Cart item = list.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvPrice.setText(String.format("￥%.2f", item.getPrice() * item.getCount()));
        Glide.with(holder.itemView).load(item.getImageResId()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.ivCover);
        holder.tvCount.setText(String.valueOf(item.getCount()));
        holder.cbCheck.setChecked(false);
        //设置选中状态
        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (onItemClickListener != null) {
                    onItemClickListener.onCheck(b, item);
                }
            }
        });
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setCount(item.getCount() + 1);
                holder.tvPrice.setText(String.format("￥%.2f", item.getPrice() * item.getCount()));
                holder.tvCount.setText(String.valueOf(item.getCount()));
                if (onItemClickListener != null) {
                    onItemClickListener.onAdd(item);
                }
            }
        });

        holder.ivCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getCount() > 1) {
                    item.setCount(item.getCount() - 1);
                    holder.tvPrice.setText(String.format("￥%.2f", item.getPrice() * item.getCount()));
                    holder.tvCount.setText(String.valueOf(item.getCount()));
                    if (onItemClickListener != null) {
                        onItemClickListener.onCut(item);
                    }
                } else {
                    list.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    if (onItemClickListener != null) {
                        onItemClickListener.onRemove(item);
                    }
                }
            }
        });
    }

    public void setList(List<Cart> list) {
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

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void remove(List<Cart> checkedList) {
        list.removeAll(checkedList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onAdd(Cart item);

        void onCut(Cart item);

        void onRemove(Cart item);

        void onCheck(boolean isChecked, Cart item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle, tvPrice, tvCount;

        private final ImageView ivCover, ivAdd, ivCut;

        private final CheckBox cbCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvCount = itemView.findViewById(R.id.tv_count);
            ivAdd = itemView.findViewById(R.id.iv_add);
            ivCut = itemView.findViewById(R.id.iv_cut);
            cbCheck = itemView.findViewById(R.id.cb_check);
        }
    }
}
