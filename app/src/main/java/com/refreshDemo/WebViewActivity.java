package com.refreshDemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ListView;

import com.GoRefresh.GoRefreshLayout;
import com.GoRefresh.googleLayout.GoogleLayout;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class WebViewActivity extends AppCompatActivity {
    ListView listview;
    SwipeRefreshLayout swipeRefreshLayout;
    GoRefreshLayout layout;
    WebView mWebView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        layout = (GoRefreshLayout) this.findViewById(R.id.cydRefreshLayout);
        GoogleLayout googleLayout = new GoogleLayout(this);
        layout.setHeaderView(googleLayout);
        googleLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        mWebView= (WebView) this.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://www.baidu.com");

    }
}
