/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huixueyun.tifenwang.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.huixueyun.tifenwang.R;
import com.huixueyun.tifenwang.core.WebViewCallback;

/**
 * @version 1.0
 * @date 16/3/2
 */
public class MainActivity extends DWebViewActivity implements WebViewCallback {
    //webview的容器视图
    private LinearLayout mContainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerCloseListener(this);
        setWebViewCallback(this);
        initView();
        initData();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mContainView = (LinearLayout) findViewById(R.id.webview_contain);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mContainView.addView(mLoadingView);
        mWebView.loadUrl("http://www.baidu.com");
    }

    @Override
    protected void onDestroy() {
        unRegisterCloseListener(this);
        mContainView.removeAllViews();
        mContainView = null;
        super.onDestroy();
    }

    @Override
    public void onReload() {
        removeAndAddView(mLoadingView);
        isLoadFinish = false;
        mWebView.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onFinish() {
        synchronized (this) {
            if (!isLoadFinish) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeAndAddView(mWebView);
                    }
                }, 1000);
            }
            isLoadFinish = true;
        }
    }

    @Override
    public void onFail() {
        removeAndAddView(mErrorView);
    }

    /**
     * 将mContainView容器中的所有视图移除，并添加新视图
     *
     * @param v 需要添加的新视图
     */
    public void removeAndAddView(View v) {
        mContainView.removeAllViews();
        mContainView.addView(v);
    }
}
