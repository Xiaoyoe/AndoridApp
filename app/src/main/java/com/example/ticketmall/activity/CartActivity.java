package com.example.ticketmall.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketmall.R;
import com.example.ticketmall.adapter.CartAdapter;
import com.example.ticketmall.data.TicketTypeEnum;
import com.example.ticketmall.entity.Cart;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.sqlite.BusinessResult;
import com.example.ticketmall.sqlite.CartDB;
import com.example.ticketmall.sqlite.OrderDB;
import com.example.ticketmall.sqlite.UserDB;
import com.gzone.university.utils.CurrentUserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车界面
 */
public class CartActivity extends AppCompatActivity {

    /**
     * 选中的购物车列表
     */
    private final List<Cart> checkedList = new ArrayList<>();
    private RecyclerView rvCart;
    private Button btnPay;
    private ImageView ivBack;
    private RadioGroup rgType;
    private CartAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        bindView();
        initView();
        initAdapter();
    }

    private void bindView() {
        rvCart = findViewById(R.id.rv_cart);
        btnPay = findViewById(R.id.btn_pay);
        ivBack = findViewById(R.id.iv_back);
        rgType = findViewById(R.id.rg_type);
    }

    private void initView() {
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkedList.clear();
                RadioButton radioButton = findViewById(i);
                String typeName = radioButton.getText().toString();
                Integer type = null;
                TicketTypeEnum ticketTypeEnum = TicketTypeEnum.getByName(typeName);
                if (ticketTypeEnum != null) {
                    type = ticketTypeEnum.getCode();
                }
                // 获取购物车列表
                BusinessResult<List<Cart>> result = CartDB.getCartList(CurrentUserUtils.getCurrentUser(User.class).getId(), type);
                if (result.isSuccess()) {
                    adapter.setList(result.getData());
                } else {
                    Toast.makeText(CartActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedList.isEmpty()) {
                    Toast.makeText(CartActivity.this, "请选择要购买的商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                pay();
            }
        });
    }

    private void pay() {
        float total = 0;
        for (Cart cart : checkedList) {
            total += cart.getCount() * cart.getPrice();
        }
        String message = String.format("当前需支付%.2f元", total);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BusinessResult<Void> result = OrderDB.addOrder(CurrentUserUtils.getCurrentUser(User.class).getId(), checkedList);
                Toast.makeText(CartActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.isSuccess()) {
                    BusinessResult<User> userBusinessResult = UserDB.getByUserId(CurrentUserUtils.getCurrentUser(User.class).getId());
                    // 更新当前用户信息
                    if (userBusinessResult.isSuccess()) {
                        User user = CurrentUserUtils.getCurrentUser(User.class);
                        user.setPoints(userBusinessResult.getData().getPoints());
                        CurrentUserUtils.setCurrentUser(user);
                    }
                    // 清空购物车
                    adapter.remove(checkedList);
                    checkedList.clear();
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    private void initAdapter() {
        BusinessResult<List<Cart>> result = CartDB.getCartList(CurrentUserUtils.getCurrentUser(User.class).getId(), null);
        adapter = new CartAdapter();
        rvCart.setAdapter(adapter);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter.setList(result.getData());
        adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onAdd(Cart item) {
                BusinessResult<Void> result = CartDB.updateCart(item.getId(), item.getCount());
                if (!result.isSuccess()) {
                    // 更新失败
                    Toast.makeText(CartActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCut(Cart item) {
                BusinessResult<Void> result = CartDB.updateCart(item.getId(), item.getCount());
                if (!result.isSuccess()) {
                    // 更新失败
                    Toast.makeText(CartActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRemove(Cart item) {
                BusinessResult<Void> result = CartDB.deleteCart(item.getId());
                Toast.makeText(CartActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCheck(boolean isChecked, Cart item) {
                if (isChecked) {
                    checkedList.add(item);
                } else {
                    checkedList.remove(item);
                }
            }
        });
    }
}
