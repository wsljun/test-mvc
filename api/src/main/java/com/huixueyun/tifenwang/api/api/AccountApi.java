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
package com.huixueyun.tifenwang.api.api;

import com.huixueyun.tifenwang.api.ApiResponse;
import com.huixueyun.tifenwang.model.model.LoginInfo;

import java.util.HashMap;

/**
 * AccountApi接口
 *
 * @version 1.0
 * @date 16/3/2
 */
public interface AccountApi {
    /**
     * 注册
     *
     * @param paramMap http请求的参数
     * @return 成功时返回：{ "code": 1, "msg":"success" }
     */
    ApiResponse<Void> register(HashMap<String, String> paramMap);

    /**
     * 登录
     *
     * @param paramMap http请求的参数
     * @return 成功时返回：{ "code": 1, "msg":"success" }
     */
    ApiResponse<LoginInfo> login(HashMap<String, String> paramMap);
}
