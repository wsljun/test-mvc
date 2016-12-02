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
package com.huixueyun.tifenwang.api.api;

import com.huixueyun.tifenwang.api.ApiResponse;
import com.huixueyun.tifenwang.api.net.HttpEngine;
import com.huixueyun.tifenwang.model.model.LoginInfo;
import com.huixueyun.tifenwang.model.utils.Constants;

import java.io.IOException;
import java.util.HashMap;

/**
 * AccountApi实现类
 *
 * @version 1.0
 * @date 16/3/2
 */
public class AccountApiImpl implements AccountApi {
    //HttpEngine对象
    private HttpEngine httpEngine;

    /**
     * 构造函数
     */
    public AccountApiImpl() {
        httpEngine = HttpEngine.getInstance();
    }

    @Override
    public ApiResponse<Void> register(HashMap<String, String> paramMap) {
        try {
            return httpEngine.postConnectionHandle(paramMap, Constants.REGISTER, httpEngine.REGISTER_URL, httpEngine.RESPONSE_TYPE_JSON);
        } catch (IOException e) {
            return new ApiResponse(Constants.TIME_OUT_EVENT, Constants.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ApiResponse<LoginInfo> login(HashMap<String, String> paramMap) {
        try {
            return httpEngine.postClientHandle(paramMap, Constants.LOGIN, httpEngine.LOGIN_URL, httpEngine.RESPONSE_TYPE_JSON);
        } catch (IOException e) {
            return new ApiResponse(Constants.TIME_OUT_EVENT, Constants.TIME_OUT_EVENT_MSG);
        }
    }
}
