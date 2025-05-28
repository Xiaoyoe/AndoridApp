package com.example.ticketmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ticketmall.R;
import com.example.ticketmall.api.ApiManager;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.utils.HttpUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    // 定义常量，用于存储视图控件的ID和提示信息
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final String ERROR_USERNAME_SHORT = "用户名长度不能少于 %d 位";
    private static final String ERROR_PASSWORD_SHORT = "密码长度不能少于 %d 位";
    private static final String ERROR_INVALID_EMAIL = "请输入有效的邮箱地址";

    private Button btnRegister;
    private EditText etUsername, etPassword, etEmail;
    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindView();
        initView();
    }

    // 绑定视图控件
    private void bindView() {
        btnRegister = findViewById(R.id.btn_register);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        ivBack = findViewById(R.id.iv_back);
    }

    // 初始化视图控件的事件监听器
    private void initView() {
        // 点击返回按钮，关闭当前页面
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 点击注册按钮
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取用户输入的所有信息
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                // 检查用户名和密码的长度
                if (username.length() < MIN_USERNAME_LENGTH) {
                    Toast.makeText(RegisterActivity.this, String.format(ERROR_USERNAME_SHORT, MIN_USERNAME_LENGTH), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < MIN_PASSWORD_LENGTH) {
                    Toast.makeText(RegisterActivity.this, String.format(ERROR_PASSWORD_SHORT, MIN_PASSWORD_LENGTH), Toast.LENGTH_SHORT).show();
                    return;
                }

                // 检查邮箱格式
                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, ERROR_INVALID_EMAIL, Toast.LENGTH_SHORT).show();
                    return;
                }

                // 调用 ApiManager 的注册接口，传递所有用户信息
                ApiManager.register(username, password, email, new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            // 解析响应的 JSON 数据
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            // 显示注册结果的提示信息
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (success) {
                                // 注册成功，创建用户对象并设置所有属性
                                User user = new User();
                                user.setUsername(username);    // 设置用户名
                                user.setPassword(password);    // 设置密码
                                user.setEmail(email);          // 设置邮箱

                                // 返回登录界面，并传递用户信息
                                Intent intent = new Intent();
                                intent.putExtra("user", user);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "解析响应数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        // 显示网络请求失败的提示信息
                        Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // 邮箱格式验证
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }
}