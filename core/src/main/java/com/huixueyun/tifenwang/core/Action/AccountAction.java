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

import com.huixueyun.tifenwang.api.ApiResponse;
import com.huixueyun.tifenwang.core.ActionCallbackListener;
import com.huixueyun.tifenwang.model.model.LoginInfo;

/**
 * AccountAction接口
 *
 * @version 1.0
 * @date 16/3/2
 */
public interface AccountAction {
    /**
     * 注册
     *
     * @param phoneNum 手机号
     * @param code     验证码
     * @param password 密码
     * @param listener 回调监听器
     */
    void register(String phoneNum, String code, String password, ActionCallbackListener<Void> listener);

    /**
     * 登录
     *
     * @param loginName 登录名
     * @param password  密码
     * @param listener  回调监听器
     */
    void login(String loginName, String password, ActionCallbackListener<ApiResponse<LoginInfo>> listener);
}
