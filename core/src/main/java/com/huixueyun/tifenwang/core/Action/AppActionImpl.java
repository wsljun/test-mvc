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
package com.huixueyun.tifenwang.core.Action;

import android.content.Context;

import com.huixueyun.tifenwang.core.Thread.ThreadPool;

/**
 * AccountAction接口的实现类
 *
 * @version 1.0
 * @date 16/3/2
 */
public class AppActionImpl implements AppAction {
    //上下文
    private Context context;

    /**
     * 构造函数
     *
     * @param context
     */
    public AppActionImpl(Context context) {
        this.context = context;
    }

    @Override
    public void stopThreadTask() {
        ThreadPool.getInstance().stopTask();
    }

    @Override
    public void stopThreadTask(String type) {
        ThreadPool.getInstance().stopTask(type);
    }

    @Override
    public void releaseThreadTask() {
        ThreadPool.getInstance().releaseTask();
    }
}
