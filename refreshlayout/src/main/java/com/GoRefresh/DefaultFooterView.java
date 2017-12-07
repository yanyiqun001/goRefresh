package com.GoRefresh;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.GoRefresh.interfaces.IFooterView;


/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class DefaultFooterView implements IFooterView {
    private Context context;
    private View loadingView;
    private View finishView;
    private View errorView;
    private int retryId;

    public DefaultFooterView(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        loadingView = LayoutInflater.from(context).inflate(R.layout.footer_loading, null);
        finishView = LayoutInflater.from(context).inflate(R.layout.footer_finish, null);
        errorView = LayoutInflater.from(context).inflate(R.layout.footer_error, null);
        retryId=R.id.tips;
        TextView retryView = (TextView) errorView.findViewById(retryId);
        retryView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
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
    public int getRetryId() {
        return retryId;
    }

//    @Override
//    public View getRetryView() {
//
//        return retryView;
//    }

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

    public void setErrorView(int errorLayoutID) {
        View errorView = LayoutInflater.from(context).inflate(errorLayoutID, null);
        this.errorView = errorView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setErrorView(View errorView, int retryId) {
        this.errorView = errorView;
        this.retryId = retryId;
    }


    public void setErrorView(int errorLayoutID, int retryId) {
        View errorView = LayoutInflater.from(context).inflate(errorLayoutID, null);
        this.errorView = errorView;
        this.retryId = retryId;
    }

}
