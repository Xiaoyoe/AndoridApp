package com.example.ticketmall.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.ticketmall.R;
import com.example.ticketmall.activity.AIChatActivity;
import com.example.ticketmall.activity.AboutActivity;
import com.example.ticketmall.activity.CartActivity;
import com.example.ticketmall.activity.ExchangeLogActivity;
import com.example.ticketmall.activity.LoginActivity;
import com.example.ticketmall.activity.OrderActivity;
import com.example.ticketmall.entity.User;
import com.gzone.university.utils.CurrentUserUtils;

import java.util.Locale;

public class UserFragment extends Fragment {

    private TextView tvUsername, tvPoints;
    private LinearLayout llCart, llOrder, llExchange, llAbout, llAiChat;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        User currentUser = CurrentUserUtils.getCurrentUser(User.class);
        tvUsername.setText(String.format(Locale.getDefault(), "用户昵称：%s", currentUser.getUsername()));
        tvPoints.setText(String.format(Locale.getDefault(), "积分：%d", currentUser.getPoints()));
    }

    private void bindView() {
        tvUsername = getView().findViewById(R.id.tv_username);
        tvPoints = getView().findViewById(R.id.tv_points);
        llCart = getView().findViewById(R.id.ll_cart);
        llOrder = getView().findViewById(R.id.ll_order);
        llExchange = getView().findViewById(R.id.ll_exchange);
        llAbout = getView().findViewById(R.id.ll_about);
        llAiChat = getView().findViewById(R.id.ll_ai_chat);
        btnLogout = getView().findViewById(R.id.btn_logout);
    }

    private void initView() {
        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });
        llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                startActivity(intent);
            }
        });
        llExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExchangeLogActivity.class);
                startActivity(intent);
            }
        });
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
        llAiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AIChatActivity.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("提示");
                builder.setMessage("确认退出登录");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
        });
    }
}
