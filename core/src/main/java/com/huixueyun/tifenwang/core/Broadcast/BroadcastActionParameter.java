package com.huixueyun.tifenwang.core.Broadcast;

import com.huixueyun.tifenwang.api.ApiResponse;

import java.io.Serializable;

public class BroadcastActionParameter implements Serializable {
    //以广播形式发送给请求对象的参数
    private ApiResponse apiResponse;

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }
}
