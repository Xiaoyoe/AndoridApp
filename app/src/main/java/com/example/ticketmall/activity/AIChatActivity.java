package com.example.ticketmall.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

public class AIChatActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView rvChat;
    private EditText etMessage;
    private Button btnSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aichat);
        bindView();
        initView();
    }

    private void bindView() {
        ivBack = findViewById(R.id.iv_back);
        rvChat = findViewById(R.id.rv_chat);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chatAdapter = new ChatAdapter(chatMessages);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(chatAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    etMessage.setText("");
                }
            }
        });
    }

    private void sendMessage(String message) {
        ChatMessage userMessage = new ChatMessage(message, ChatMessage.SENDER_USER);
        chatMessages.add(userMessage);

        // 添加一条“思考中...”的 AI 消息
        ChatMessage thinkingMessage = new ChatMessage("", ChatMessage.SENDER_AI);
        thinkingMessage.setStatus(ChatMessage.STATUS_PENDING);
        chatMessages.add(thinkingMessage);

        chatAdapter.notifyDataSetChanged();
        // 调用 AI 接口获取回复
        getAIResponse(message);
    }

    private void getAIResponse(String message) {
        ApiManager.chat(message, new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                    String answer = jsonObject.get("answer").getAsString();

                    // 在主线程更新 UI
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // 找到最后一条“思考中...”的 AI 消息并更新
                            for (int i = chatMessages.size() - 1; i >= 0; i--) {
                                ChatMessage message = chatMessages.get(i);
                                if (message.getSender() == ChatMessage.SENDER_AI && message.getStatus() == ChatMessage.STATUS_PENDING) {
                                    message.setMessage(answer);
                                    message.setStatus(ChatMessage.STATUS_COMPLETED);
                                    break;
                                }
                            }
                            chatAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    // 处理解析异常，在主线程更新 UI
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // 找到最后一条“思考中...”的 AI 消息并更新
                            for (int i = chatMessages.size() - 1; i >= 0; i--) {
                                ChatMessage message = chatMessages.get(i);
                                if (message.getSender() == ChatMessage.SENDER_AI && message.getStatus() == ChatMessage.STATUS_PENDING) {
                                    message.setMessage("解析响应数据失败: " + e.getMessage());
                                    message.setStatus(ChatMessage.STATUS_COMPLETED);
                                    break;
                                }
                            }
                            chatAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                // 在主线程更新 UI
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // 找到最后一条“思考中...”的 AI 消息并更新
                        for (int i = chatMessages.size() - 1; i >= 0; i--) {
                            ChatMessage message = chatMessages.get(i);
                            if (message.getSender() == ChatMessage.SENDER_AI && message.getStatus() == ChatMessage.STATUS_PENDING) {
                                message.setMessage("请求失败: " + errorMsg);
                                message.setStatus(ChatMessage.STATUS_COMPLETED);
                                break;
                            }
                        }
                        chatAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}