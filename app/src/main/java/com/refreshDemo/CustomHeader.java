package com.refreshDemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.GoRefresh.IHeaderView;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

public class CustomHeader implements IHeaderView {
    private Context mContext;
    private View mView;
    private TextView mTextView;
    private ImageView icon;
    private ObjectAnimator animator ;
    private MulRingProgressBar progressBar;
    public CustomHeader(Context context) {
        this.mContext=context;
        mView=LayoutInflater.from(context).inflate(R.layout.customheader,null);
        mTextView=mView.findViewById(R.id.text);
        icon=mView.findViewById(R.id.arrow);
        progressBar=mView.findViewById(R.id.progressview);
        icon.setImageResource(R.drawable.arrow);
        icon.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mTextView.setText("下拉刷新");
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void onPull(float a) {
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onChange(boolean isPull) {
        if(isPull){//下拉经过临界点
            mTextView.setText("松手刷新");
            arrowUp();
        }else{ //上拉经过临界点
            mTextView.setText("下拉刷新");
            arrowDown();
        }

    }

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.start();
        icon.setVisibility(View.GONE);
        mTextView.setText("正在加载");

    }

    @Override
    public void onRefreshFinish() {

    }

    @Override
    public void onBackFinish() {
        reset();

    }
    private void arrowDown(){
        icon.animate().rotation(0).setDuration(300).start();
    }
    private void arrowUp(){
        icon.animate().rotation(-180).setDuration(300).start();
    }


    private void reset() {
        mTextView.setText("下拉刷新");
        progressBar.setVisibility(View.GONE);
        progressBar.stop();
        icon.setVisibility(View.VISIBLE);
        icon.setImageResource(R.drawable.arrow);
        icon.setRotation(0);
    }

}
