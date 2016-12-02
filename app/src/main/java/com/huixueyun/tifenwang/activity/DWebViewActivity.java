package com.huixueyun.tifenwang.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.huixueyun.tifenwang.R;
import com.huixueyun.tifenwang.core.WebViewCallback;

public class DWebViewActivity extends DBaseActivity {
    //webview对象
    public WebView mWebView;
    //WebViewCallback对象
    public WebViewCallback mWebViewCallback = null;
    //加载失败视图
    public View mErrorView;
    //加载中视图
    public View mLoadingView;
    //是否加载失败
    public boolean isLoadFail = false;
    //是否加载完成
    public boolean isLoadFinish = false;
    //加载失败提示
    private TextView mErrorTextView;
    //LayoutInflater对象
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStatusView();
        setStatusViewParams();
        initWebView();
    }

    /**
     * 设置状态视图的参数
     */
    private void setStatusViewParams() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mErrorView.setLayoutParams(params);
        mLoadingView.setLayoutParams(params);
    }

    /**
     * 初始化状态视图
     */
    private void initStatusView() {
        mInflater = LayoutInflater.from(getApplicationContext());
        mErrorView = mInflater.inflate(R.layout.layout_error, null);
        mLoadingView = mInflater.inflate(R.layout.layout_loading, null);
        mErrorTextView = (TextView) mErrorView.findViewById(R.id.error);

        mErrorTextView.setOnClickListener(new CustomOnClickListener());
    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        mWebView = new WebView(getApplicationContext());
        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setBuiltInZoomControls(true);
        setting.setAppCacheEnabled(true);
        setting.setDisplayZoomControls(true);

        mWebView.setWebViewClient(new CustomWebViewClient());
    }

    /**
     * 设置webview加载状态的回调
     *
     * @param callback
     */
    public void setWebViewCallback(WebViewCallback callback) {
        mWebViewCallback = callback;
    }

    @Override
    protected void onDestroy() {
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
        mWebViewCallback = null;
        super.onDestroy();
    }

    //自定义加载失败的点击事件
    class CustomOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mWebViewCallback != null)
                mWebViewCallback.onReload();
        }
    }

    //自定义webview加载状态
    class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isLoadFail = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!isLoadFail) {
                if (mWebViewCallback != null) {
                    mWebViewCallback.onFinish();
                }
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            isLoadFail = true;
            if (mWebViewCallback != null)
                mWebViewCallback.onFail();
        }
    }
}
