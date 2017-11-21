package com.GoRefesh_core;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class DefaultFooterView implements IFooterView {
    private Context context;
    private View loadingView;
    private View finishView;
    private View errorView;
    private View retryView;

    public DefaultFooterView(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        loadingView = LayoutInflater.from(context).inflate(R.layout.footer_loading, null);
        finishView = LayoutInflater.from(context).inflate(R.layout.footer_finish, null);
        errorView = LayoutInflater.from(context).inflate(R.layout.footer_error, null);
        retryView = errorView.findViewById(R.id.tips);
        ((TextView) retryView).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public View getLoadingView() {
        return loadingView;
    }

    @Override
    public View getFinishView() {
        return finishView;
    }

    @Override
    public View getFailureView() {
        return errorView;
    }

    @Override
    public View getRetryView() {

        return retryView;
    }

    public void setLoadingView(int layoutID) {
        this.loadingView = LayoutInflater.from(context).inflate(layoutID, null);
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    public void setFinishView(int layoutID) {
        this.finishView = LayoutInflater.from(context).inflate(layoutID, null);
    }

    public void setFinishView(View finishView) {
        this.finishView = finishView;
    }

    public void setErrorView(int layoutID) {
        View errorView = LayoutInflater.from(context).inflate(layoutID, null);
        this.errorView = errorView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setErrorView(int layoutID, int retryId) {
        View errorView = LayoutInflater.from(context).inflate(layoutID, null);
        View retryView = errorView.findViewById(retryId);
        this.errorView = errorView;
        this.retryView = retryView;
    }

    public void setErrorView(View errorView, View retryView) {
        this.errorView = errorView;
        this.retryView = retryView;
    }
}
