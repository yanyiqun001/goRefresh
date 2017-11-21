package com.refreshDemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.GoRefresh.GoRefreshLayout;
import com.GoRefresh.RefreshListener;
import com.GoRefresh.googleLayout.GoogleLayout;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class ScrollerViewActivity extends AppCompatActivity {
    GoRefreshLayout goRefreshLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollerview);
        goRefreshLayout = this.findViewById(R.id.cydRefreshLayout);
        GoogleLayout googleLayout = new GoogleLayout(this);
        goRefreshLayout.setHeaderView(googleLayout);
        googleLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        goRefreshLayout.setFixedContent(true);
        goRefreshLayout.setHasFooter(true);
        goRefreshLayout.setDurationFooterHidden(200  );
        goRefreshLayout.setOnRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goRefreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });
        goRefreshLayout.setDuration_autotoRefreshHeight(500);
        goRefreshLayout.startRefresh();
    }
}
