package com.refreshDemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.GoRefresh.GoRefreshLayout;
import com.GoRefresh.interfaces.RefreshListener;
import com.GoRefresh.googleLayout.GoogleLayout;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class WebViewActivity extends AppCompatActivity {
    ListView listview;
    SwipeRefreshLayout swipeRefreshLayout;
    GoRefreshLayout refreshlayout;
    WebView mWebView;
    Handler handler=new Handler();
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        refreshlayout = (GoRefreshLayout) this.findViewById(R.id.cydRefreshLayout);
        GoogleLayout googleLayout = new GoogleLayout(this);
        refreshlayout.setHeaderView(googleLayout);
        googleLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        mWebView= (WebView) this.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        refreshlayout.startRefresh();
        refreshlayout.setOnRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("https://www.baidu.com");
                    }
                },1000);
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                refreshlayout.finishRefresh();
                super.onPageFinished(view, url);
            }
        });
    }
}
