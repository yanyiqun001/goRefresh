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
    public CustomFooter(Context context) {
        inflater=LayoutInflater.from(context);
    }

    @Override
    public View getLoadingView() {
        return inflater.inflate(R.layout.lottle_loading_animation_footer,null);
    }

    @Override
    public View getFinishView() {
        return inflater.inflate(R.layout.footer_finish,null);
    }

    @Override
    public View getFailureView() {
        return inflater.inflate(R.layout.footer_error,null);
    }

    @Override
    public int getRetryId() {
        return R.id.tips;
    }


}
