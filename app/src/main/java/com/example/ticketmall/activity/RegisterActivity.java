package com.example.ticketmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ticketmall.R;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.sqlite.BusinessResult;
import com.example.ticketmall.sqlite.UserDB;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;

    private EditText etUsername, etPassword, etPasswordAgain;

    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindView();
        initView();
    }

    private void bindView() {
        btnRegister = findViewById(R.id.btn_register);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etPasswordAgain = findViewById(R.id.et_password_again);
        ivBack = findViewById(R.id.iv_back);
    }

    private void initView() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String passwordAgain = etPasswordAgain.getText().toString();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                BusinessResult<User> result = UserDB.register(user, passwordAgain);
                Toast.makeText(RegisterActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.isSuccess()) {
                    // 注册成功，返回登录界面，并传递用户信息
                    Intent intent = new Intent();
                    intent.putExtra("user", user);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
