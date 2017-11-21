package com.refreshDemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.GoRefesh_core.LottieView;
import com.GoRefresh.GoRefreshLayout;
import com.GoRefresh.LoadmoreListener;
import com.GoRefresh.RefreshListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        refreshLayout = this.findViewById(R.id.layout);
        recyclerView = this.findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //  GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        addData();
        CustomHeader customHeader = new CustomHeader(this);
        refreshLayout.setHeaderView(customHeader);
        refreshLayout.setHasFooter(true);
        refreshLayout.setOnRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }
                }, 4000);
            }
        });
        refreshLayout.setDurationFooterVisiable(200);
        refreshLayout.setDurationFooterHidden(200);
        refreshLayout.setOnLoadmoreListener(new LoadmoreListener() {
            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        page++;
                        if (page == 3) {
                            refreshLayout.finishLoadmoreWithError();
                            page++;
                        }
                        if (page == 4) {
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
        refreshLayout.startRefresh();
    }

    private void addData() {
        DataSource.addData(list);
        adapter.notifyItemRangeChanged(list.size() - COUNT, list.size());
    }


    class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(RecyclerViewActivity.this).inflate(R.layout.item_rv, parent, false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            Glide.with(holder.itemView.getContext()).load(list.get(position)).crossFade().into(holder.imageView);
//            int randomheight = (int) (1 + Math.random() * 600);
//            holder.itemView.getLayoutParams().height= randomheight;
//            holder.textView.setText(""+position);
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
