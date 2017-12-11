package com.refreshDemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.GoRefresh.interfaces.IFooterView;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class CustomFooter implements IFooterView {
    private LayoutInflater inflater;
    private View mLoadingView;
    private View mErrorview;
    private View mFinishView;
    public CustomFooter(Context context) {
        inflater=LayoutInflater.from(context);
        mErrorview=inflater.inflate(R.layout.footerview_error,null);
        mLoadingView=inflater.inflate(R.layout.lottle_loading_animation_footer,null);
        mFinishView=inflater.inflate(R.layout.footer_finish,null);
    }

    @Override
    public View getLoadingView() {
        return mLoadingView;
    }

    @Override
    public View getFinishView() {
        return mFinishView;
    }

    @Override
    public View getFailureView() {
        return mErrorview;
    }

    @Override
    public int getRetryId() {
        return R.id.tv_retry;
    }


}
