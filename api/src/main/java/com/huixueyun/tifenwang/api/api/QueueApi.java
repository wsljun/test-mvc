package com.huixueyun.tifenwang.api.api;

import com.huixueyun.tifenwang.api.ApiResponse;

import java.util.HashMap;

/**
 * QueueApi接口
 *
 * @version 1.0
 * @date 16/3/2
 */
public interface QueueApi {
    /**
     * 发送行为
     *
     * @param paramMap 行为统计请求的参数
     * @return 成功时返回：{ "code": 1, "msg":"success" }
     */
    ApiResponse<Void> sendBehavior(HashMap<String, String> paramMap);
}
