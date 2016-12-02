package com.huixueyun.tifenwang.api;

import com.huixueyun.tifenwang.model.model.LoginInfo;
import com.huixueyun.tifenwang.model.utils.Constants;

import org.json.JSONObject;

public class ApiModel {
    //ApiModel实例
    private static ApiModel instance = null;

    //构造函数
    public ApiModel() {

    }

    //ApiModel单例
    public static ApiModel getInstance() {
        if (instance == null) {
            synchronized (ApiModel.class) {
                if (instance == null)
                    instance = new ApiModel();
            }
        }

        return instance;
    }

    /**
     * 获取ApiResponse
     *
     * @param type 请求类型
     * @param data 服务器返回数据
     * @return 相对应的ApiResponse
     * @throws Exception
     */
    public ApiResponse getApiResponse(String type, String data) throws Exception {
        String event = "0";
        String msg = "请求错误，请稍后重试！";
        JSONObject object = new JSONObject(data);
        if (object.has("success"))
            event = object.getString("success");
        if (object.has("message"))
            msg = object.getString("message");

        if (type.equals(Constants.LOGIN)) {
            return getLoginApiResponse(new ApiResponse<LoginInfo>(event, msg), object);
        }

        return null;
    }

    /**
     * 获取LoginApiResponse
     *
     * @param apiResponse LoginApiResponse
     * @param object      服务器返回数据
     * @return LoginApiResponse
     * @throws Exception
     */
    private ApiResponse<LoginInfo> getLoginApiResponse(ApiResponse<LoginInfo> apiResponse, JSONObject object) throws Exception {
        LoginInfo info = new LoginInfo();
        if (object.has("user_id"))
            info.setUser_id(object.getString("user_id"));
        if (object.has("xueduan"))
            info.setXueduan(object.getString("xueduan"));
        if (object.has("type"))
            info.setType(object.getString("type"));
        if (object.has("student_name"))
            info.setStudent_name(object.getString("student_name"));
        if (object.has("student_phone"))
            info.setStudent_phone(object.getString("student_phone"));
        if (object.has("qq"))
            info.setQq(object.getString("qq"));
        if (object.has("parent_phone"))
            info.setParent_phone(object.getString("parent_phone"));
        if (object.has("defaultlsSet"))
            info.setDefaultlsSet(object.getString("defaultlsSet"));
        apiResponse.setObj(info);

        return apiResponse;
    }
}
