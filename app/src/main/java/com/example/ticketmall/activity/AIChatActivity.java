package com.example.ticketmall.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ticketmall.R;
import com.example.ticketmall.adapter.ChatAdapter;
import com.example.ticketmall.api.ApiManager;
import com.example.ticketmall.entity.ChatMessage;
import com.example.ticketmall.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AIChatActivity extends AppCompatActivity {

    private ImageView ivBack;
    private ImageView ivClearHistory;
    private RecyclerView rvChat;
    private EditText etMessage;
    private Button btnSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private static final String PREF_NAME = "ChatPrefs";
    private static final String CHAT_MESSAGES_KEY = "chat_messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aichat);
        bindView();
        loadChatMessages();
        initView();
    }

    private void bindView() {
        ivBack = findViewById(R.id.iv_back);
        ivClearHistory = findViewById(R.id.iv_clear_history);
        rvChat = findViewById(R.id.rv_chat);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
    }

    private void initView() {
        ivBack.setOnClickListener(view -> finish());

        ivClearHistory.setOnClickListener(view -> clearChatHistory());

        chatAdapter = new ChatAdapter(chatMessages);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(chatAdapter);

        // 加载完数据后滚动到底部
        if (chatMessages.size() > 0) {
            rvChat.scrollToPosition(chatMessages.size() - 1);
        }

        btnSend.setOnClickListener(view -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                etMessage.setText("");
            }
        });
    }

    private void clearChatHistory() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要清除所有聊天记录吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    chatMessages.clear();
                    chatAdapter.notifyDataSetChanged();

                    SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                    prefs.edit().remove(CHAT_MESSAGES_KEY).apply();

                    Toast.makeText(this, "聊天记录已清除", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void sendMessage(String message) {
        // 禁用发送按钮
        btnSend.setEnabled(false);
        btnSend.setAlpha(0.5f); // 设置半透明效果，表示禁用状态

        ChatMessage userMessage = new ChatMessage(message, ChatMessage.SENDER_USER, 0);
        chatMessages.add(userMessage);
        chatAdapter.notifyDataSetChanged();
        rvChat.smoothScrollToPosition(chatMessages.size() - 1);

        ChatMessage thinkingMessage = new ChatMessage("思考中...", ChatMessage.SENDER_AI, 0);
        int thinkingMessageIndex = chatMessages.size();
        chatMessages.add(thinkingMessage);
        chatAdapter.notifyDataSetChanged();
        rvChat.smoothScrollToPosition(chatMessages.size() - 1);

        getAIResponse(message, thinkingMessageIndex);
    }

    private void getAIResponse(String message, int thinkingMessageIndex) {
        ApiManager.chat(message, new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                    String answer = jsonObject.get("answer").getAsString();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        chatMessages.get(chatMessages.size() - 2).status = 1;
                        chatMessages.set(thinkingMessageIndex, new ChatMessage(answer, ChatMessage.SENDER_AI, 1));
                        chatAdapter.notifyDataSetChanged();
                        saveChatMessages();
                        rvChat.smoothScrollToPosition(chatMessages.size() - 1);

                        // 启用发送按钮
                        btnSend.setEnabled(true);
                        btnSend.setAlpha(1.0f); // 恢复完全不透明
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        chatMessages.get(chatMessages.size() - 2).status = 2;
                        chatMessages.set(thinkingMessageIndex, new ChatMessage("解析响应数据失败: " + e.getMessage(), ChatMessage.SENDER_AI, 2));
                        chatAdapter.notifyDataSetChanged();
                        saveChatMessages();
                        rvChat.smoothScrollToPosition(chatMessages.size() - 1);

                        // 启用发送按钮
                        btnSend.setEnabled(true);
                        btnSend.setAlpha(1.0f);
                    });
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    chatMessages.get(chatMessages.size() - 2).status = 2;
                    chatMessages.set(thinkingMessageIndex, new ChatMessage("请求失败: " + errorMsg, ChatMessage.SENDER_AI, 2));
                    chatAdapter.notifyDataSetChanged();
                    saveChatMessages();
                    rvChat.smoothScrollToPosition(chatMessages.size() - 1);

                    // 启用发送按钮
                    btnSend.setEnabled(true);
                    btnSend.setAlpha(1.0f);
                });
            }
        });
    }

    private void loadChatMessages() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(CHAT_MESSAGES_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ChatMessage>>() {}.getType();
            chatMessages = gson.fromJson(json, type);
        }
    }

    private void saveChatMessages() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(chatMessages);
        editor.putString(CHAT_MESSAGES_KEY, json);
        editor.apply();
    }
}