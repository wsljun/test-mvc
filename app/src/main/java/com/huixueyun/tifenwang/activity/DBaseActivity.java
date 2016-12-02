/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huixueyun.tifenwang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.huixueyun.tifenwang.DApplication;
import com.huixueyun.tifenwang.R;
import com.huixueyun.tifenwang.core.Action.AccountAction;
import com.huixueyun.tifenwang.core.Action.AppAction;
import com.huixueyun.tifenwang.core.Service.QueueAction;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Activity抽象基类
 *
 * @version 1.0
 * @date 16/3/2
 */
public abstract class DBaseActivity extends FragmentActivity {
    // 上下文实例
    public Context context;
    // 应用全局的实例
    public DApplication application;
    // 核心层的accountAction实例
    public AccountAction accountAction;
    // 核心层的appAction实例
    public AppAction appAction;
    // 队列任务的Action实例
    public QueueAction queueAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        application = (DApplication) this.getApplication();
        accountAction = application.getAccountAction();
        appAction = application.getAppAction();
        queueAction = application.getQueueAction();
    }

    /**
     * 获取所有未关闭的activity的对象集合
     *
     * @return 所有未关闭的activity的对象集合
     */
    public CopyOnWriteArrayList<Activity> getCloseListeners() {
        return application.getCloseListeners();
    }

    /**
     * 注册当前activity
     *
     * @param at activity对象
     */
    public void registerCloseListener(Activity at) {
        if (getCloseListeners() != null) {
            getCloseListeners().add(at);
        }
    }

    /**
     * 解注册activity
     *
     * @param at activity对象
     */
    public void unRegisterCloseListener(Activity at) {
        if (getCloseListeners() != null && getCloseListeners().contains(at)) {
            getCloseListeners().remove(at);
        }
    }

    /**
     * 启动Activity并关闭上一个Activity
     *
     * @param cls
     */
    public void startActivityAndFinish(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        closeActivity();
    }

    /**
     * 启动Activity，不关闭上一个Activity
     *
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }

    /**
     * 关闭当前Activity
     */
    public void closeActivity() {
        finish();
    }
}
