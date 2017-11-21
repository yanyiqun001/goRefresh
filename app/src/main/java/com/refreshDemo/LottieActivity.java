package com.refreshDemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.GoRefresh.GoRefreshLayout;
import com.GoRefresh.LoadmoreListener;
import com.GoRefresh.LottieView;

import com.GoRefresh.RefreshListener;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class LottieActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GoRefreshLayout refreshLayout;
    private MyAdapter adapter;
    private List<Integer> list = new ArrayList<>();
    private final int COUNT = 10;
    private LottieView lottieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        refreshLayout = this.findViewById(R.id.layout);
        recyclerView = this.findViewById(R.id.rv);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        addData();

        lottieView = new LottieView(this, R.layout.lottie_gift, R.id.animation_view);
        lottieView.setPullOriginProgress(1f);
        refreshLayout.setHeaderView(lottieView);

        refreshLayout.setOnRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                    }
                }, 4000);
            }
        });
        refreshLayout.setHasFooter(true);
        refreshLayout.setOnLoadmoreListener(new LoadmoreListener() {
            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
    }

    private void addData() {
        DataSource.addData(list);
        adapter.notifyItemRangeChanged(list.size() - COUNT, list.size());
    }


    //添加 menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //menu的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1: //简单使用
                lottieView = new LottieView(this, R.layout.lottle_loading_animation, R.id.animation_view);
                lottieView.setRefreshDuration(1000);//动画时长
                break;
            case R.id.item2: //自定义
                lottieView = new MyLottileView(this, R.layout.lottie_oval, R.id.animation_view);
                break;
            case R.id.item3: //截取指定部分动画
                lottieView = new LottieView(this, R.layout.lottie_loading, R.id.animation_view);
                lottieView.setPullProgressRange(0f, 0.7f);  //设置下拉过程中动画变化范围（0f-1f）默认无动画
                lottieView.setRefreshProgressRange(0.1f, 0.66f); //设置刷新过程中动画变化范围（0f-1f） 默认0f-1f
                lottieView.setRefreshDuration(1200); //动画时长
                break;
            case R.id.item4:
                lottieView = new LottieView(this, R.layout.lottie_gift, R.id.animation_view);
                lottieView.setPullOriginProgress(1f);//设置下拉过程中始终保持在某一帧（0f-1f）
                break;
            case R.id.item5: //占满屏幕宽度
                WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(dm);
                lottieView = new LottieView(this, R.layout.lottie_wave, R.id.animation_view);
                LottieAnimationView view = lottieView.getLottieView();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.height = dm.heightPixels;
                layoutParams.width = dm.widthPixels;
                view.setLayoutParams(layoutParams);
                break;
            case R.id.item6:
                lottieView = new LottieView(this, R.layout.lottie_jump_loader, R.id.animation_view);
                break;

            case R.id.item7:
                lottieView = new LottieView(this, R.layout.lottie_simple, R.id.animation_view);
                lottieView.setPullProgressRange(0f, 1f);
                break;
            case R.id.item8:
                lottieView = new LottieView(this, R.layout.lottie_cycle, R.id.animation_view);
                lottieView.setRefreshDuration(2000);
                break;
            case R.id.item9:
                lottieView = new LottieView(this, R.layout.lottie_worm, R.id.animation_view);
                break;
            case R.id.item10:
                lottieView = new MyLottileView(this, R.layout.lottie_material_wave_loading, R.id.animation_view);
                break;
            case R.id.item11:
                lottieView = new LottieView(this, R.layout.lottie_json, R.id.animation_view);
                lottieView.setRefreshProgressRange(0f, 0.5f);
                lottieView.setPullOriginProgress(0.45f);
                break;

        }
        refreshLayout.setHeaderView(lottieView);
        return true;
    }

    class MyAdapter extends RecyclerView.Adapter<MyHoler> {
        @Override
        public MyHoler onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHoler(LayoutInflater.from(LottieActivity.this).inflate(R.layout.item_rv, parent, false));
        }

        @Override
        public void onBindViewHolder(MyHoler holder, final int position) {
            Glide.with(holder.itemView.getContext()).load(list.get(position)).crossFade().into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    static class MyHoler extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyHoler(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            imageView = itemView.findViewById(R.id.img);
        }
    }
}
