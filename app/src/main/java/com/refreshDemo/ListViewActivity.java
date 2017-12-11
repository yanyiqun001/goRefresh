package com.refreshDemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.GoRefresh.GoRefreshLayout;
import com.GoRefresh.interfaces.LoadMoreListener;
import com.GoRefresh.interfaces.RefreshListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/11 0011.
 */

public class ListViewActivity extends AppCompatActivity {
    private GoRefreshLayout goRefreshLayout;
    private ListView listView;
    private MyAdapter adapter = new MyAdapter();
    private List<Integer> list = new ArrayList<>();
    private int page = 1;
    private Handler handler=new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        goRefreshLayout = this.findViewById(R.id.goRefreshLayout);
        listView = this.findViewById(R.id.listView);
        addData();
        if (list.size() >= 10) {
            goRefreshLayout.setHasFooter(true);
        }
        listView.setAdapter(adapter);

        CustomFooter customFooter = new CustomFooter(this);
        goRefreshLayout.setFooterView(customFooter);

        goRefreshLayout.setLoadingView(R.layout.footer_loading);
        goRefreshLayout.setErrorViewWithRetry(R.layout.footerview_error, R.id.tips);

        //下拉刷新
        goRefreshLayout.setOnRefreshListener( new RefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        addData();
                        adapter.notifyDataSetChanged();
                        goRefreshLayout.finishRefresh();
                        page=1;
                    }
                }, 2000);
            }
        });
        goRefreshLayout.startRefresh();
        //上拉加载
        goRefreshLayout.setOnLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadmore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 2) {
                            //加载错误时调用
                            goRefreshLayout.finishLoadMoreWithError();
                            page++;
                            return;
                        }
                        if (page == 3) {
                            //无更多数据时调用
                            goRefreshLayout.finishLoadMoreWithNoData();
                            return;
                        }
                        page++;
                        addData();
                        //加载完成时调用
                        goRefreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });

        //如需设置listView的滚动监听 使用此方法
        goRefreshLayout.setScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(ListViewActivity.this, i + "", Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    private void addData() {
        DataSource.addData(list);
        adapter.notifyDataSetChanged();
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ListViewActivity.this).inflate(R.layout.item_lv, viewGroup, false);
                holder.textview = convertView.findViewById(R.id.textview);
                holder.imageView = convertView.findViewById(R.id.imageview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textview.setText("你的名字" + i);
            Glide.with(ListViewActivity.this).load(list.get(i)).crossFade().into(holder.imageView);
            return convertView;
        }

        class ViewHolder {
            TextView textview;
            ImageView imageView;
        }
    }


}
