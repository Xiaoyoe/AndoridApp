package com.example.ticketmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ticketmall.R;
import com.example.ticketmall.entity.ChatMessage;
import java.util.List;

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
        if (message.getSender() == ChatMessage.SENDER_USER) {
            // 如果是用户消息，显示用户消息布局，隐藏 AI 消息布局
            holder.tvMessageUser.setVisibility(View.VISIBLE);
            holder.tvMessageAi.setVisibility(View.GONE);
            holder.tvMessageUser.setText(message.getMessage());
            holder.tvStatusUser.setText(getStatusText(message.getStatus()));
        } else {
            // 如果是 AI 消息，显示 AI 消息布局，隐藏用户消息布局
            holder.tvMessageAi.setVisibility(View.VISIBLE);
            holder.tvMessageUser.setVisibility(View.GONE);
            holder.tvMessageAi.setText(message.getMessage());
            holder.tvStatusAi.setText(getStatusText(message.getStatus()));
        }
    }

    private String getStatusText(int status) {
        switch (status) {
            case 0:
                return "发送中";
            case 1:
                return "发送成功";
            case 2:
                return "发送失败";
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageAi;
        TextView tvMessageUser;
        TextView tvStatusAi;
        TextView tvStatusUser;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageAi = itemView.findViewById(R.id.tv_message_ai);
            tvMessageUser = itemView.findViewById(R.id.tv_message_user);
            tvStatusAi = itemView.findViewById(R.id.tv_status_ai);
            tvStatusUser = itemView.findViewById(R.id.tv_status_user);
        }
    }
}