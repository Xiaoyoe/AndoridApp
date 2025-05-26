package com.example.ticketmall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ticketmall.R;
import com.example.ticketmall.entity.Ticket;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.sqlite.BusinessResult;
import com.example.ticketmall.sqlite.CartDB;
import com.gzone.university.utils.CurrentUserUtils;

/**
 * 商品详情界面
 */
public class DetailActivity extends AppCompatActivity {

    private ImageView ivBack, ivCover;

    private TextView tvTitle, tvScore, tvContent1, tvContent2, tvPrice;

    private Button btnAdd;

    private Ticket ticket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ticket = (Ticket) getIntent().getSerializableExtra("ticket");

        bindView();
        initView();
    }

    private void bindView() {
        ivBack = findViewById(R.id.iv_back);
        ivCover = findViewById(R.id.iv_cover);
        tvTitle = findViewById(R.id.tv_title);
        tvScore = findViewById(R.id.tv_score);
        tvContent1 = findViewById(R.id.tv_content1);
        tvContent2 = findViewById(R.id.tv_content2);
        tvPrice = findViewById(R.id.tv_price);
        btnAdd = findViewById(R.id.btn_add);
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Glide.with(this).load(ticket.getImageResId()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivCover);
        tvTitle.setText(ticket.getTitle());
        tvScore.setText(ticket.getScore());
        tvContent1.setText(ticket.getContent1());
        tvContent2.setText(ticket.getContent2());

        //保留两位小数
        String price = String.format("￥%.2f", ticket.getPrice());
        tvPrice.setText(price);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加购物车
                BusinessResult<Void> result = CartDB.addCart(CurrentUserUtils.getCurrentUser(User.class).getId(), ticket);
                Toast.makeText(DetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
