package com.example.ticketmall.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketmall.R;
import com.example.ticketmall.adapter.StuffAdapter;
import com.example.ticketmall.data.AppData;
import com.example.ticketmall.entity.Exchange;
import com.example.ticketmall.entity.Stuff;
import com.example.ticketmall.entity.User;
import com.example.ticketmall.sqlite.BusinessResult;
import com.example.ticketmall.sqlite.ExchangeDB;
import com.example.ticketmall.sqlite.UserDB;
import com.gzone.university.utils.CurrentUserUtils;

import java.util.List;

public class ExchangeFragment extends Fragment {

    private RecyclerView rvExchange;
    private StuffAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initAdapter();
        // 调用 loadData 方法加载商品数据
        loadData();
    }

    private void initView() {
        rvExchange = getView().findViewById(R.id.rv_exchange);
    }

    private void initAdapter() {
        adapter = new StuffAdapter(getContext()); // 传入上下文
        rvExchange.setAdapter(adapter);
        rvExchange.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int spacing = (int) (getResources().getDisplayMetrics().density * 16);
        rvExchange.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));
        adapter.setOnItemClickListener(new StuffAdapter.OnItemClickListener() {
            @Override
            public void onExchangeClick(Stuff item) {
                Exchange exchange = new Exchange();
                exchange.setUserId(CurrentUserUtils.getCurrentUser(User.class).getId());
                exchange.setPoints(item.getPoints());
                exchange.setStuffName(item.getName());
                BusinessResult<Void> result = ExchangeDB.addExchange(exchange);
                Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.isSuccess()) {
                    BusinessResult<User> userBusinessResult = UserDB.getByUserId(CurrentUserUtils.getCurrentUser(User.class).getId());
                    // 更新当前用户信息
                    if (userBusinessResult.isSuccess()) {
                        User user = CurrentUserUtils.getCurrentUser(User.class);
                        user.setPoints(userBusinessResult.getData().getPoints());
                        CurrentUserUtils.setCurrentUser(user);
                    }
                }
            }
        });
    }

    /**
     * 加载商品数据的方法
     */
    private void loadData() {
        // 调用 AppData 的 getStuffList 方法获取商品列表
        AppData.getStuffList(new AppData.DataCallback<List<Stuff>>() {
            @Override
            public void onSuccess(List<Stuff> stuffList) {
                // 请求成功，将获取到的商品列表设置给适配器
                adapter.setList(stuffList);
            }

            @Override
            public void onFailure(String errorMsg) {
                // 处理请求失败的情况，这里可以添加更多的错误处理逻辑，例如显示错误提示
                Toast.makeText(getContext(), "加载商品数据失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 网格布局的间距装饰器
     */
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}