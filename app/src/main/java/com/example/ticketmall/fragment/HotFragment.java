package com.example.ticketmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketmall.R;
import com.example.ticketmall.activity.DetailActivity;
import com.example.ticketmall.adapter.SingleItemAdapter;
import com.example.ticketmall.data.AppData;
import com.example.ticketmall.data.TicketTypeEnum;
import com.example.ticketmall.entity.Ticket;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.sqlite.BusinessResult;
import com.example.ticketmall.sqlite.CartDB;
import com.gzone.university.utils.CurrentUserUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HotFragment extends Fragment {

    private RecyclerView rvHot;

    private LinearLayout llTab;

    private SingleItemAdapter adapter;

    // 当前选中的tab，默认为全部
    private TicketTypeEnum currentTicketType = TicketTypeEnum.ALL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initAdapter();
        initTabListeners();
        updateTabStyles();
        loadData();
    }

    private void initView() {
        rvHot = getView().findViewById(R.id.rv_hot);
        llTab = getView().findViewById(R.id.ll_tab);
    }

    private void initAdapter() {
        adapter = new SingleItemAdapter();
        rvHot.setAdapter(adapter);
        rvHot.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnItemClickListener(new SingleItemAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(Ticket item) {
                //添加购物车
                BusinessResult<Void> result = CartDB.addCart(CurrentUserUtils.getCurrentUser(User.class).getId(), item);
                Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(Ticket item) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("ticket", item);
                startActivity(intent);
            }
        });
    }

    private void initTabListeners() {
        // 给每个tab设置点击监听器
        for (int i = 0; i < llTab.getChildCount(); i++) {
            LinearLayout tab = (LinearLayout) llTab.getChildAt(i);
            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) tab.getChildAt(1);
                    currentTicketType = TicketTypeEnum.getByName(textView.getText().toString());
                    updateTabStyles();
                    loadData();
                }
            });
        }
    }

    private void updateTabStyles() {
        for (int i = 0; i < llTab.getChildCount(); i++) {
            LinearLayout tab = (LinearLayout) llTab.getChildAt(i);
            TextView textView = (TextView) tab.getChildAt(1);
            String tabText = textView.getText().toString();
            TicketTypeEnum ticketType = TicketTypeEnum.getByName(tabText);
            // 如果当前tab文本与当前选中的tab文本相同，则设置为选中状态
            if (Objects.equals(ticketType, currentTicketType)) {
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                tab.setBackgroundResource(R.drawable.bg_tab_selected);
            } else {
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                tab.setBackgroundResource(R.drawable.bg_tab_normal);
            }
        }
    }

    private void loadData() {
        List<Ticket> dataList;
        switch (currentTicketType) {
            case CONCERT:
                dataList = AppData.getConcertList();
                break;
            case MUSIC:
                dataList = AppData.getMusicFestivalList();
                break;
            case COMEDY:
                dataList = AppData.getComedyShowList();
                break;
            case MOVIE:
                dataList = AppData.getMovieList();
                break;
            default:
                //获取所有类型的数据
                dataList = new ArrayList<>();
                dataList.addAll(AppData.getMovieList());
                dataList.addAll(AppData.getComedyShowList());
                dataList.addAll(AppData.getMusicFestivalList());
                dataList.addAll(AppData.getConcertList());
                //打乱顺序
                Collections.shuffle(dataList);
                break;
        }
        adapter.setList(dataList);
    }
}
