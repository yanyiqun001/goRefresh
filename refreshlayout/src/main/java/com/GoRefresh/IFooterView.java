package com.GoRefresh;

import android.view.View;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public interface IFooterView {
    View getLoadingView();

    View getFinishView();

    View getFailureView();

    View getRetryView();



}
