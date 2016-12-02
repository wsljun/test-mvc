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
import android.text.TextUtils;

import com.huixueyun.tifenwang.api.ApiResponse;
import com.huixueyun.tifenwang.api.api.AccountApi;
import com.huixueyun.tifenwang.api.api.AccountApiImpl;
import com.huixueyun.tifenwang.core.ActionCallbackListener;
import com.huixueyun.tifenwang.core.ErrorEvent;
import com.huixueyun.tifenwang.core.Thread.ThreadPool;
import com.huixueyun.tifenwang.model.model.LoginInfo;
import com.huixueyun.tifenwang.model.security.MD5Util;
import com.huixueyun.tifenwang.model.utils.Constants;
import com.huixueyun.tifenwang.model.utils.SystemUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AccountAction接口的实现类
 *
 * @version 1.0
 * @date 16/3/2
 */
public class AccountActionImpl implements AccountAction {
    //AccountActionImpl对象
    private static AccountActionImpl instance;
    //AccountApi对象
    private static AccountApi api;
    //上下文
    private Context context;

    //构造函数
    public AccountActionImpl(Context context) {
        this.context = context;
        this.api = new AccountApiImpl();
    }

    //AccountActionImpl单例
    public static AccountActionImpl getAppActionImplInstance(Context context) {
        if (instance == null) {
            synchronized (AccountActionImpl.class) {
                if (instance == null)
                    instance = new AccountActionImpl(context);
            }
        }
        return instance;
    }

    /**
     * 获取AccountApi对象
     *
     * @return AccountApi对象
     */
    public static AccountApi getAccountApi() {
        return api;
    }

    @Override
    public void register(final String phoneNum, final String code, final String password, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "验证码为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码为空");
            }
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
    }

    @Override
    public void login(final String loginName, final String password, final ActionCallbackListener<ApiResponse<LoginInfo>> listener) {
        // 参数检查
        if (TextUtils.isEmpty(loginName)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "登录名为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码为空");
            }
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("username", loginName);
        map.put("password", password);
        map.put("userId", "");
        map.put("channelId", "");
        map.put("terminal", "a");
        map.put("model", SystemUtil.getDeviceId()); //获取手机型号
        map.put("release", SystemUtil.getSdkVersionName() + ","
                + SystemUtil.getOsVersionName());//RELEASE获取版本号
        map.put("mk", MD5Util.md5(Constants.MD5String));
        ThreadPool.getInstance().addTask(context, Constants.LOGIN, map, listener);
    }
}
