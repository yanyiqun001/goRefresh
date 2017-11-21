package com.refreshDemo;

import android.content.Context;
import android.widget.TextView;

import com.GoRefesh_core.LottieView;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 有的资源文件周围有比较夸张的留白 并不适合与其他view在一起布局
 */

public class MyLottileView extends LottieView {
    TextView textView;

    public MyLottileView(Context context, int layoutId, int lottieViewId) {
        super(context, layoutId, lottieViewId);
        textView = getView().findViewById(R.id.tv);
    }

    @Override
    public void onPull(float a) {
        super.onPull(a);
    }

    @Override
    public void onReady() {
        super.onReady();
    }

    @Override
    public void onChange(boolean isPull) {
        super.onChange(isPull);
        //true表示下拉经过临界 false表示上拉经过临界
        if (isPull) {
            textView.setText(R.string.release);
        } else {
            textView.setText(R.string.pull);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        textView.setText(R.string.refrshing);
    }

    @Override
    public void onRefreshFinish() {
        super.onRefreshFinish();
    }

    @Override
    public void onBackFinish() {
        super.onBackFinish();
        textView.setText(R.string.pull);
    }
}
