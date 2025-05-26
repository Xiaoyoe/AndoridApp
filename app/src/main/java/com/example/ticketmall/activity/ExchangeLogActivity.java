package com.example.ticketmall.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketmall.R;
import com.example.ticketmall.adapter.ExchangeAdapter;
import com.example.ticketmall.entity.Exchange;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.sqlite.BusinessResult;
import com.example.ticketmall.sqlite.ExchangeDB;
import com.gzone.university.utils.CurrentUserUtils;

public class ExchangeLogActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView rvExchange;
    private ExchangeAdapter exchangeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_log);
        bindView();
        initData();
        initView();
    }

    private void bindView() {
        ivBack = findViewById(R.id.iv_back);
        rvExchange = findViewById(R.id.rv_exchange);
    }

    private void initData() {
        exchangeAdapter = new ExchangeAdapter();
        exchangeAdapter.setOnItemClickListener(new ExchangeAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position, Exchange item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExchangeLogActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确认删除兑换记录");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BusinessResult<Void> result = ExchangeDB.deleteExchange(item.getId());
                        Toast.makeText(ExchangeLogActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        if (result.isSuccess()) {
                            exchangeAdapter.remove(position);
                        }
                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
        });
        // 设置兑换记录列表
        exchangeAdapter.setList(ExchangeDB.getExchangeList(CurrentUserUtils.getCurrentUser(User.class).getId()).getData());
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rvExchange.setLayoutManager(new LinearLayoutManager(this));
        rvExchange.setAdapter(exchangeAdapter);
    }
} 