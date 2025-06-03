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
        rvHot = requireView().findViewById(R.id.rv_hot);
        llTab = requireView().findViewById(R.id.ll_tab);
    }

    private void initAdapter() {
        adapter = new SingleItemAdapter();
        rvHot.setAdapter(adapter);
        rvHot.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setOnItemClickListener(new SingleItemAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(Ticket item) {
                addToCart(item);
            }

            @Override
            public void onItemClick(Ticket item) {
                navigateToDetail(item);
            }
        });
    }

    private void addToCart(Ticket item) {
        BusinessResult<Void> result = CartDB.addCart(
                CurrentUserUtils.getCurrentUser(User.class).getId(),
                item
        );
        Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void navigateToDetail(Ticket item) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("ticket", item);
        startActivity(intent);
    }

    private void initTabListeners() {
        for (int i = 0; i < llTab.getChildCount(); i++) {
            LinearLayout tab = (LinearLayout) llTab.getChildAt(i);
            tab.setOnClickListener(v -> {
                TextView textView = (TextView) tab.getChildAt(1);
                currentTicketType = TicketTypeEnum.getByName(textView.getText().toString());
                updateTabStyles();
                loadData();
            });
        }
    }

    private void updateTabStyles() {
        for (int i = 0; i < llTab.getChildCount(); i++) {
            LinearLayout tab = (LinearLayout) llTab.getChildAt(i);
            TextView textView = (TextView) tab.getChildAt(1);
            TicketTypeEnum ticketType = TicketTypeEnum.getByName(textView.getText().toString());

            if (Objects.equals(ticketType, currentTicketType)) {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                tab.setBackgroundResource(R.drawable.bg_tab_selected);
            } else {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                tab.setBackgroundResource(R.drawable.bg_tab_normal);
            }
        }
    }

    /**
     * 统一数据加载方法
     */
    private void loadData() {
        switch (currentTicketType) {
            case CONCERT:
                loadConcertData();
                break;
            case MUSIC:
                loadMusicFestivalData();
                break;
            case COMEDY:
                loadComedyShowData();
                break;
            case MOVIE:
                loadMovieData();
                break;
            default:
                loadAllData();
        }
    }

    private void loadConcertData() {
        AppData.getConcertList(new AppData.DataCallback<List<Ticket>>() {
            @Override
            public void onSuccess(List<Ticket> data) {
                adapter.setList(data);
            }

            @Override
            public void onFailure(String errorMsg) {
                showError("演唱会数据加载失败", errorMsg);
            }
        });
    }

    private void loadMusicFestivalData() {
        AppData.getMusicFestivalList(new AppData.DataCallback<List<Ticket>>() {
            @Override
            public void onSuccess(List<Ticket> data) {
                adapter.setList(data);
            }

            @Override
            public void onFailure(String errorMsg) {
                showError("音乐节数据加载失败", errorMsg);
            }
        });
    }

    private void loadComedyShowData() {
        AppData.getComedyShowList(new AppData.DataCallback<List<Ticket>>() {
            @Override
            public void onSuccess(List<Ticket> data) {
                adapter.setList(data);
            }

            @Override
            public void onFailure(String errorMsg) {
                showError("脱口秀数据加载失败", errorMsg);
            }
        });
    }

    private void loadMovieData() {
        AppData.getMovieList(new AppData.DataCallback<List<Ticket>>() {
            @Override
            public void onSuccess(List<Ticket> data) {
                adapter.setList(data);
            }

            @Override
            public void onFailure(String errorMsg) {
                showError("电影数据加载失败", errorMsg);
            }
        });
    }

    private void loadAllData() {
        AppData.getTicketList(new AppData.DataCallback<List<Ticket>>() {
            @Override
            public void onSuccess(List<Ticket> allTickets) {
                // 打乱顺序增加随机性
                Collections.shuffle(allTickets);
                adapter.setList(allTickets);
            }

            @Override
            public void onFailure(String errorMsg) {
                showError("票务数据加载失败", errorMsg);
            }
        });
    }

    private void showError(String title, String errorMsg) {
        Toast.makeText(getContext(), title + ": " + errorMsg, Toast.LENGTH_SHORT).show();
        adapter.setList(new ArrayList<>()); // 清空列表显示
    }
}