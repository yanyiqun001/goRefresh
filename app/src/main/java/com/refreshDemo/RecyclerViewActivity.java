package com.refreshDemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.GoRefesh_core.LottieView;
import com.GoRefresh.GoRefreshLayout;
import com.GoRefresh.RvLoadMoreWrapper;
import com.GoRefresh.interfaces.LoadMoreListener;
import com.GoRefresh.interfaces.RefreshListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GoRefreshLayout refreshLayout;
    private MyAdapter adapter;
    private List<Integer> list = new ArrayList<>();
    private final int COUNT = 10;
    private LottieView lottieView;
    private int page = 1;
    private RvLoadMoreWrapper rvLoadMoreWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        refreshLayout = this.findViewById(R.id.layout);
        recyclerView = this.findViewById(R.id.rv);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
         StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //  GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new MyAdapter();

        //自定义header
        CustomHeader customHeader = new CustomHeader(this);
        refreshLayout.setHeaderView(customHeader);

        //需要上拉刷新必须设置
        refreshLayout.setHasFooter(true);


        //自定义footer
        CustomFooter customFooter = new CustomFooter(this);

        //设置footerview方式1 直接设置
        refreshLayout.setFooterView(customFooter);
        //只设置加载状态的footerview
        // refreshLayout.setLoadingView(R.layout.lottle_loading_animation_footer);

        //上拉加载通过包装adapter实现 通过此方法获得包装后的adapter
        rvLoadMoreWrapper=refreshLayout.buildRvLoadMoreAdapter(adapter);

        //设置footerview方式2 通过包装后的adapter设置
        rvLoadMoreWrapper.setFooterView(customFooter);

        //只设置加载状态的footerview
        // rvLoadMoreWrapper.setLoadingView(R.layout.lottle_loading_animation_footer);

        recyclerView.setAdapter(rvLoadMoreWrapper);


        refreshLayout.setOnRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });

        refreshLayout.setOnLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        if (page == 3) {
                            refreshLayout.finishLoadmoreWithError();
                            return;
                        }
                        if (page == 5) {
                            refreshLayout.finishLoadmoreWithNoData();
                            return;
                        } else {
                            addData();
                            refreshLayout.finishLoadmore();
                        }

                    }
                }, 2000);
            }
        });
//        rvLoadMoreWrapper.setLoadMoreListener(new LoadMoreListener() {
//            @Override
//            public void onLoadmore() {
//
//            }
//        });

        //自动刷新
        refreshLayout.startRefresh();
        addData();
    }

    private void addData() {
        DataSource.addData(list);
        rvLoadMoreWrapper.notifyItemRangeChanged(list.size() - COUNT, list.size());
    }


    class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(RecyclerViewActivity.this).inflate(R.layout.item_rv, parent, false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            Glide.with(holder.itemView.getContext()).load(list.get(position)).crossFade().into(holder.imageView);
            int randomheight = (int) (400+ Math.random() * 200);
            holder.imageView.getLayoutParams().height= randomheight;
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(RecyclerViewActivity.this,position+"",Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            imageView = itemView.findViewById(R.id.img);
        }
    }
}
