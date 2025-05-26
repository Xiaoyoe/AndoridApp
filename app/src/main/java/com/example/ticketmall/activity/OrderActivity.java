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
import com.example.ticketmall.adapter.OrderAdapter;
import com.example.ticketmall.entity.Order;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.sqlite.BusinessResult;
import com.example.ticketmall.sqlite.OrderDB;
import com.gzone.university.utils.CurrentUserUtils;

public class OrderActivity extends AppCompatActivity {

    private ImageView ivBack;

    private RecyclerView rvOrder;

    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        //绑定控件
        bindView();
        initData();
        //初始化控件
        initView();
    }

    private void bindView() {
        ivBack = findViewById(R.id.iv_back);
        rvOrder = findViewById(R.id.rv_order);
    }

    private void initData() {
        orderAdapter = new OrderAdapter();
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position, Order item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确认删除订单");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //删除订单
                        BusinessResult<Void> result = OrderDB.deleteOrder(item.getId());
                        Toast.makeText(OrderActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        if (result.isSuccess()) {
                            orderAdapter.remove(position);
                        }
                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
        });
        //设置订单列表
        orderAdapter.setList(OrderDB.getOrderList(CurrentUserUtils.getCurrentUser(User.class).getId()).getData());
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        rvOrder.setAdapter(orderAdapter);
    }
}
