package com.GoRefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.GoRefresh.interfaces.IHeaderView;
import com.GoRefresh.weight.RingProgressBar;


/**
 * Created by Administratoron 2017/9/25 0025.
 */

public class DefaultHeaderLayout implements IHeaderView {
    private View mHeaderView;
    private ImageView icon;
    private TextView text;
    private Context context;
    private RingProgressBar progressBar;

    public DefaultHeaderLayout(@NonNull Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.headerview, null);
        icon = (ImageView) mHeaderView.findViewById(R.id.arrow);
        text = (TextView) mHeaderView.findViewById(R.id.text);
        progressBar = (RingProgressBar) mHeaderView.findViewById(R.id.progressbar);
        icon.setVisibility(View.VISIBLE);
        icon.setImageResource(R.drawable.arrow);
    }

    @Override
    public View getView() {
        return mHeaderView;
    }

    @Override
    public void onPull(float a) {

    }

    @Override
    public void onReady() {
    }

    @Override
    public void onChange(boolean isPull) {
        if (isPull) {
            text.setText(R.string.release);
            arrowUp();
        } else {
            text.setText(R.string.pulltorefresh);
            arrowDown();
        }
    }

    @Override
    public void onRefresh() {
        text.setText(R.string.loading);
        iconChange();
    }

    @Override
    public void onRefreshFinish() {
    }

    @Override
    public void onBackFinish() {
        reset();
    }

    private void arrowDown() {
        icon.animate().rotation(0).setDuration(300).start();
    }

    private void arrowUp() {
        icon.animate().rotation(-180).setDuration(300).start();
    }

    private void iconChange() {
        icon.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation();
    }

    private void reset() {
        text.setText(R.string.pulltorefresh);
        icon.setVisibility(View.VISIBLE);
        icon.setRotation(0);
        progressBar.setVisibility(View.GONE);
        progressBar.stopAnimation();
    }

}
