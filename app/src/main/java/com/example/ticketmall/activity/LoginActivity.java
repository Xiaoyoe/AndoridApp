package com.example.ticketmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ticketmall.R;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.sqlite.BusinessResult;
import com.example.ticketmall.sqlite.UserDB;
import com.gzone.university.utils.CurrentUserUtils;

public class LoginActivity extends AppCompatActivity {

    /**
     * 登录按钮,注册按钮
     */
    private Button btnLogin, btnRegister;

    /**
     * 用户名输入框,密码输入框
     */
    private EditText etUsername, etPassword;

    /**
     * 记住密码复选框
     */
    private CheckBox cbRemember;

    /**
     * 界面跳转回调
     */
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 绑定控件
        bindView();
        // 初始化控件
        initView();
    }

    private void bindView() {
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        cbRemember = findViewById(R.id.cb_remember);
    }

    private void initView() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            // 注册成功
                            User user = (User) result.getData().getSerializableExtra("user");
                            etUsername.setText(user.getUsername());
                            etPassword.setText(user.getPassword());
                        }
                    }
                }
        );

        // 获取当前用户
        User currentUser = CurrentUserUtils.getCurrentUser(User.class);
        if (currentUser.getRemember()) {
            // 如果用户选择了记住密码，则填充用户名和密码
            etUsername.setText(currentUser.getUsername());
            etPassword.setText(currentUser.getPassword());
            cbRemember.setChecked(true);
        } else {
            // 否则只填充用户名
            etUsername.setText(currentUser.getUsername());
            cbRemember.setChecked(false);
        }
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // 记住密码
                User user = CurrentUserUtils.getCurrentUser(User.class);
                user.setRemember(b);
                CurrentUserUtils.setCurrentUser(user);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                BusinessResult<User> login = UserDB.login(user);
                if (login.isSuccess()) {
                    // 登录成功
                    login.getData().setRemember(cbRemember.isChecked());
                    CurrentUserUtils.setCurrentUser(login.getData());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 登录失败
                    Toast.makeText(LoginActivity.this, login.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
    }
}