package com.example.ticketmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ticketmall.R;
import com.example.ticketmall.entity.ChatMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        Date timestamp = message.getTimestamp();

        // 设置时区为北京时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String timeString = sdf.format(timestamp);

        if (message.getSender() == ChatMessage.SENDER_USER) {
            // 如果是用户消息，显示用户消息布局，隐藏 AI 消息布局
            holder.tvMessageUser.setVisibility(View.VISIBLE);
            holder.tvMessageAi.setVisibility(View.GONE);
            holder.tvTimeUser.setVisibility(View.VISIBLE);
            holder.tvTimeAi.setVisibility(View.GONE);
            holder.tvThinkingAi.setVisibility(View.GONE);
            holder.tvMessageUser.setText(message.getMessage());
            holder.tvTimeUser.setText(timeString);
        } else {
            // 如果是 AI 消息，显示 AI 消息布局，隐藏用户消息布局
            if (message.getStatus() == ChatMessage.STATUS_PENDING) {
                holder.tvMessageAi.setVisibility(View.GONE);
                holder.tvTimeAi.setVisibility(View.GONE);
                holder.tvThinkingAi.setVisibility(View.VISIBLE);
            } else {
                holder.tvMessageAi.setVisibility(View.VISIBLE);
                holder.tvTimeAi.setVisibility(View.VISIBLE);
                holder.tvThinkingAi.setVisibility(View.GONE);
                holder.tvMessageAi.setText(message.getMessage());
                holder.tvTimeAi.setText(timeString);
            }
            holder.tvMessageUser.setVisibility(View.GONE);
            holder.tvTimeUser.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageAi;
        TextView tvMessageUser;
        TextView tvTimeAi;
        TextView tvTimeUser;
        TextView tvThinkingAi;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageAi = itemView.findViewById(R.id.tv_message_ai);
            tvMessageUser = itemView.findViewById(R.id.tv_message_user);
            tvTimeAi = itemView.findViewById(R.id.tv_time_ai);
            tvTimeUser = itemView.findViewById(R.id.tv_time_user);
            tvThinkingAi = itemView.findViewById(R.id.tv_thinking_ai);
        }
    }
}