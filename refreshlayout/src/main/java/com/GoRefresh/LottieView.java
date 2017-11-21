package com.GoRefresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class LottieView implements IHeaderView {
    private View view;
    private LottieAnimationView animationView;
    private ValueAnimator valueAnimator;

    private float mPullFrom = 0.0f;
    private float mPullTo = 0.0f;
    private float mRefreshFrom = 0.0f;
    private float mRefreshTo = 1.0f;
    private int duration;

    public LottieView(Context context, int layoutId, int lottieViewId) {
        initView(context, layoutId, lottieViewId);

    }

    private void initView(Context context, int layoutId, int lottieViewId) {
        view = LayoutInflater.from(context).inflate(layoutId, null);
        animationView = view.findViewById(lottieViewId);

    }

    public void setAnimation(String filename){
        animationView.setAnimation(filename);
    }


    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onPull(float a) {
        float progress = culProgress(mPullFrom, mPullTo, a);
        animationView.setProgress(progress);
    }


    @Override
    public void onReady() {

    }


    @Override
    public void onChange(boolean isPull) {

    }


    @Override
    public void onRefresh() {
        valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(duration==0?animationView.getDuration():duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animationView.setProgress(culProgress(mRefreshFrom, mRefreshTo, (Float) valueAnimator.getAnimatedValue()));
            }
        });
        valueAnimator.setRepeatCount(-1);
        valueAnimator.start();
    }


    @Override
    public void onRefreshFinish() {

    }


    @Override
    public void onBackFinish() {
        animationView.cancelAnimation();
        if (valueAnimator != null)
            valueAnimator.cancel();

    }


    public LottieAnimationView getLottieView() {
        return animationView;
    }

    private float culProgress(float from, float to, float percent) {
        return from  +percent * (to - from);
    }

    public void setPullProgressRange(float from, float to) {
        mPullFrom = from;
        mPullTo = to;
    }

    public void setPullProgressToEnd(float from) {
        mPullFrom = from;
        mPullTo = 1.0f;
    }

    public void setPullOriginProgress(float from) {
        mPullFrom = from;
        mPullTo = from;
    }

    public void setRefreshProgressRange(float from, float to) {
        mRefreshFrom = from;
        mRefreshTo = to;
    }

    public void setRefreshProgressToEnd(float from) {
        mRefreshFrom = from;
        mRefreshTo = from;
    }

    public void setRefreshDuration(int duration) {
        this.duration = duration;

    }
}
