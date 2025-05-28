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
import com.example.ticketmall.api.ApiManager;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.utils.HttpUtils;
import com.gzone.university.utils.CurrentUserUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
        // 绑定界面上的控件到对应的变量
        bindView();
        // 初始化控件的相关操作，如设置监听器、填充已有数据等
        initView();
    }

    /**
     * 绑定界面上的控件到对应的变量
     */
    private void bindView() {
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        cbRemember = findViewById(R.id.cb_remember);
    }

    /**
     * 初始化控件的相关操作，如设置监听器、填充已有数据等
     */
    private void initView() {
        // 注册一个用于启动新 Activity 并获取结果的回调
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // 如果从注册界面返回且结果为成功
                        if (result.getResultCode() == RESULT_OK) {
                            // 从返回的数据中获取注册的用户信息
                            User user = (User) result.getData().getSerializableExtra("user");
                            // 将注册的用户名填充到用户名输入框
                            etUsername.setText(user.getUsername());
                            // 将注册的密码填充到密码输入框
                            etPassword.setText(user.getPassword());
                        }
                    }
                }
        );

        // 获取当前存储的用户信息
        User currentUser = CurrentUserUtils.getCurrentUser(User.class);
        if (currentUser.getRemember()) {
            // 如果用户之前选择了记住密码，则填充用户名和密码
            etUsername.setText(currentUser.getUsername());
            etPassword.setText(currentUser.getPassword());
            // 将记住密码复选框设置为选中状态
            cbRemember.setChecked(true);
        } else {
            // 否则只填充用户名
            etUsername.setText(currentUser.getUsername());
            // 将记住密码复选框设置为未选中状态
            cbRemember.setChecked(false);
        }

        // 为记住密码复选框设置状态改变监听器
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // 获取当前存储的用户信息
                User user = CurrentUserUtils.getCurrentUser(User.class);
                // 设置用户的记住密码状态
                user.setRemember(b);
                // 更新存储的用户信息
                CurrentUserUtils.setCurrentUser(user);
            }
        });

        // 为登录按钮设置点击监听器
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取用户在输入框中输入的用户名
                String username = etUsername.getText().toString();
                // 获取用户在输入框中输入的密码
                String password = etPassword.getText().toString();

                // 调用 ApiManager 的登录接口进行登录操作
                ApiManager.login(username, password, new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            // 解析响应的 JSON 数据
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success) {
                                // 登录成功
                                User user = null;
                                if (jsonObject.has("data")) {
                                    user = new Gson().fromJson(jsonObject.getString("data"), User.class);
                                } else {
                                    // 如果没有 data 字段，可以根据实际情况处理，这里简单创建一个新的 User 对象
                                    user = new User();
                                    user.setUsername(etUsername.getText().toString());
                                    user.setPassword(etPassword.getText().toString());
                                }
                                user.setRemember(cbRemember.isChecked());
                                CurrentUserUtils.setCurrentUser(user);

                                // 跳转到主界面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // 登录失败，显示错误信息
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "解析响应数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        // 如果网络请求失败，显示失败的提示信息
                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 为注册按钮设置点击监听器
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建一个跳转到注册界面的 Intent
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                // 启动注册界面 Activity 并等待结果
                activityResultLauncher.launch(intent);
            }
        });
    }
}