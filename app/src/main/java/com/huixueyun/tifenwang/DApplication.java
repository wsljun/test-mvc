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
package com.huixueyun.tifenwang;

import android.app.Activity;
import android.app.Application;

import com.huixueyun.tifenwang.core.Action.AccountAction;
import com.huixueyun.tifenwang.core.Action.AccountActionImpl;
import com.huixueyun.tifenwang.core.Action.AppAction;
import com.huixueyun.tifenwang.core.Action.AppActionImpl;
import com.huixueyun.tifenwang.core.Service.QueueAction;
import com.huixueyun.tifenwang.core.Service.QueueActionImpl;
import com.huixueyun.tifenwang.model.utils.DisplayUtil;
import com.huixueyun.tifenwang.model.utils.L;
import com.huixueyun.tifenwang.model.utils.SystemUtil;
import com.huixueyun.tifenwang.model.utils.ValueStorage;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Application类，应用级别的操作都放这里
 *
 * @version 1.0
 * @date 16/3/2
 */
public class DApplication extends Application {
    //账号action
    private AccountAction accountAction;
    //应用action
    private AppAction appAction;
    //队列任务action
    private QueueAction queueAction;
    //activity对象集合
    private volatile CopyOnWriteArrayList<Activity> closeListeners = new CopyOnWriteArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        accountAction = new AccountActionImpl(this);
        appAction = new AppActionImpl(this);
        queueAction = new QueueActionImpl(this);

        SystemUtil.init(this);
        DisplayUtil.init(this);
        ValueStorage.init(this);
        L.enableDebug();
    }

    /**
     * 获取账号相关的action
     *
     * @return 账号相关的action
     */
    public AccountAction getAccountAction() {
        return accountAction;
    }

    /**
     * 获取应用相关的action
     *
     * @return 应用相关的action
     */
    public AppAction getAppAction() {
        return appAction;
    }

    /**
     * 获取队列任务相关的action
     *
     * @return 队列任务相关的action
     */
    public QueueAction getQueueAction() {
        return queueAction;
    }

    /**
     * 获取所有未关闭的activity的对象集合
     *
     * @return 所有未关闭的activity的对象集合
     */
    public CopyOnWriteArrayList<Activity> getCloseListeners() {
        if (closeListeners == null) {
            closeListeners = new CopyOnWriteArrayList<>();
        }
        return closeListeners;
    }

    /**
     * 退出系统
     */
    public void exitSystem() {
        for (Activity at : closeListeners) {
            if (!at.isDestroyed()) {
                at.finish();
            }
        }

        //强制退出当前进程，以确保App本身资源得到释放
        System.exit(0);
    }
}
