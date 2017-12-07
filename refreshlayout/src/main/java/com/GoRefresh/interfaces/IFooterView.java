package com.GoRefresh.interfaces;

import android.view.View;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public interface IFooterView {
    /**
     * 加载状态view
     * @return
     */
    View getLoadingView();
    /**
     * 完成状态view
     * @return
     */
    View getFinishView();
    /**
     * 失败状态view
     * @return
     */
    View getFailureView();
    /**
     * 点击重试view的id
     * @return
     */
    int getRetryId();



}
