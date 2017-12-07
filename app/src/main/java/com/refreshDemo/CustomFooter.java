package com.refreshDemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.GoRefresh.DefaultFooterView;

/**
 * Created by Administrator on 2017/11/24 0024.
 * 这里继承了DefaultFooterView 也可直接实现IFooterView接口
 */

public class CustomFooter extends DefaultFooterView {
    private LayoutInflater inflater;
    public CustomFooter(Context context) {
        super(context);
        inflater=LayoutInflater.from(context);
    }

    @Override
    public View getLoadingView() {
        return inflater.inflate(R.layout.lottle_loading_animation_footer,null);
    }

    @Override
    public View getFinishView() {
        return super.getFinishView();
    }

    @Override
    public View getFailureView() {
        return super.getFailureView();
    }


}
