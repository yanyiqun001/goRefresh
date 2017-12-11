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



        //自定义footer
        CustomFooter customFooter = new CustomFooter(this);

        //设置footerview方式1
        // refreshLayout.setHasFooter(true);
        // refreshLayout.setFooterView(customFooter);
        //设置加载状态的footerview
        // refreshLayout.setLoadingView(R.layout.lottle_loading_animation_footer);

        //上拉加载通过包装adapter实现 通过此方法获得包装后的adapter
        rvLoadMoreWrapper=refreshLayout.buildRvLoadMoreAdapter(adapter);
        rvLoadMoreWrapper.setHasFooter(true)
                 .setFooterView(customFooter)
//                 .setLoadingView(R.layout.lottle_loading_animation_footer)
//                 .setFinishView(R.layout.footer_finish)
//                 .setErrorViewWithRetry(R.layout.footer_error,R.id.tips)
                 .setLoadMoreListener(new LoadMoreListener() {
                     @Override
                     public void onLoadmore() {
                         new Handler().postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                 page++;
                                 if (page == 3) {
                                     rvLoadMoreWrapper.finishLoadMoreWithError();
                                     return;
                                 }
                                 if (page == 5) {
                                     rvLoadMoreWrapper.finishLoadMoreWithNoData();
                                     return;
                                 } else {
                                     addData();
                                     rvLoadMoreWrapper.finishLoadMore();
                                 }
                             }
                         }, 2000);
                     }
                 });

        recyclerView.setAdapter(rvLoadMoreWrapper);

        //设置下拉监听
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

        //设置上拉监听
//        refreshLayout.setOnLoadMoreListener(new LoadMoreListener() {
//            @Override
//            public void onLoadmore() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page++;
//                        if (page == 3) {
//                            refreshLayout.finishLoadmoreWithError();
//                            return;
//                        }
//                        if (page == 5) {
//                            refreshLayout.finishLoadmoreWithNoData();
//                            return;
//                        } else {
//                            addData();
//                            refreshLayout.finishLoadmore();
//                        }
//
//                    }
//                }, 2000);
//            }
//        });


        //自动刷新
      //  refreshLayout.startRefresh();
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
