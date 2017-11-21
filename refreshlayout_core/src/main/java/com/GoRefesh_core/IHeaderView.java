package com.GoRefesh_core;

import android.view.View;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public interface IHeaderView {
    View getView();

    /**
     * 下拉过程中调用（持续触发）
     * @param a 根据下拉距离返回0-1之前的值
     */
    void onPull(float a);
    /**
     * 下拉通过临界值 处于就绪状态时调用（持续触发）
     */
    void onReady();

    /**
     * 经过临界值是调用（非持续触发）
     * @param isPull  true表示下拉经过临界 false表示上拉经过临界
     */
    void onChange(boolean isPull);

    /**
     * 开始刷新时触发（非持续触发）
     */
    void onRefresh();
    /**
     * 刷新完成时触发（非持续触发）
     */
    void onRefreshFinish();
    /**
     * 刷新完成回到顶部时触发（非持续触发）
     */
    void onBackFinish();
}
