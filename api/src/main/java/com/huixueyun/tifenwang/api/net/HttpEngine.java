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
package com.huixueyun.tifenwang.api.net;

import com.huixueyun.tifenwang.api.ApiModel;
import com.huixueyun.tifenwang.api.ApiResponse;
import com.huixueyun.tifenwang.model.utils.L;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http引擎处理类
 *
 * @version 1.0
 * @date 16/3/2
 */
public class HttpEngine {
    //登陆地址
    public final static String LOGIN_URL = "xitong/userLogin_app.php";
    //注册地址
    public final static String REGISTER_URL = "xitong/userRegisterApp.php";
    //行为地址
    public final static String BEHAVIOR_URL = "";
    //http请求数据类型
    public final static String RESPONSE_TYPE_JSON = "json";
    //Tag
    private final static String TAG = "HttpEngine";
    //服务器地址
    private final static String SERVER_URL = "http://api.huixueyuan.cn/ifdood_dev01/v2/";
    //http请求类型
    private final static String REQUEST_MOTHOD = "POST";
    //编码格式
    private final static String ENCODE_TYPE = "UTF-8";
    //超时时间
    private final static short TIME_OUT = 3000;
    //HttpEngine对象
    private static HttpEngine instance = null;

    //构造函数
    private HttpEngine() {

    }

    //HttpEngine单例
    public static HttpEngine getInstance() {
        if (instance == null) {
            synchronized (HttpEngine.class) {
                if (instance == null)
                    instance = new HttpEngine();
            }
        }
        return instance;
    }

    /**
     * http连接 urlconnection方式
     *
     * @param paramsMap 请求接口参数
     * @param model     请求接口类型
     * @param paramUrl  请求接口地址
     * @param type      返回数据类型
     * @param <T>
     * @return ApiResponse类型请求结果数据
     * @throws IOException
     */
    public <T> T postConnectionHandle(Map<String, String> paramsMap, String model, String paramUrl, String type) throws IOException {
        String data = joinParams(paramsMap, type);
        // 打印出请求
        L.e(data);
        HttpURLConnection connection = getConnection(paramUrl);
        connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
        connection.connect();
        OutputStream os = connection.getOutputStream();
        os.write(data.getBytes());
        os.flush();
        if (connection.getResponseCode() == 200) {
            // 获取响应的输入流对象
            InputStream is = connection.getInputStream();
            // 创建字节输出流对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 定义读取的长度
            int len = 0;
            // 定义缓冲区
            byte buffer[] = new byte[1024];
            // 按照缓冲区的大小，循环读取
            while ((len = is.read(buffer)) != -1) {
                // 根据读取的长度写入到os对象中
                baos.write(buffer, 0, len);
            }
            // 释放资源
            is.close();
            baos.close();
            connection.disconnect();
            // 返回字符串
            final String result = new String(baos.toByteArray());

            ApiResponse apiResponse;
            try {
                apiResponse = ApiModel.getInstance().getApiResponse(model, result);
            } catch (Exception e) {
                apiResponse = null;
            }

            return (T) apiResponse;
        } else {
            connection.disconnect();
            return null;
        }
    }

    /**
     * http连接 client
     *
     * @param paramsMap 请求接口参数
     * @param model     请求接口类型
     * @param paramUrl  请求接口地址
     * @param type      返回数据类型
     * @param <T>
     * @return ApiResponse类型请求结果数据
     * @throws IOException
     */
    public <T> T postClientHandle(Map<String, String> paramsMap, String model, String paramUrl, String type) throws IOException {
        String data = joinParams(paramsMap, type);
        // 打印出请求
        L.e(data);
        String uriAPI = SERVER_URL + paramUrl;
        /*建立HTTP Post连线*/
        HttpPost httpRequest = new HttpPost(uriAPI);
        HttpParams httpparams = new BasicHttpParams();
        /* 超时设置 */
        /* 从连接池中取连接的超时时间 */
        ConnManagerParams.setTimeout(httpparams, TIME_OUT);
        /* 连接超时 */
        HttpConnectionParams.setConnectionTimeout(httpparams, TIME_OUT);
        /* 请求超时 */
        HttpConnectionParams.setSoTimeout(httpparams, TIME_OUT);
        HttpClient httpclient = new DefaultHttpClient(httpparams);
        //Post运作传送变数必须用NameValuePair[]阵列储存
        //传参数 服务端获取的方法为request.getParameter("name")
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("json", data.substring(type.length() + 1)));

        //发出HTTP request
        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        //取得HTTP response
        HttpResponse httpResponse = httpclient.execute(httpRequest);

        //若状态码为200 ok
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            //取出回应字串
            String result = EntityUtils.toString(httpResponse.getEntity());
            ApiResponse apiResponse;
            try {
                apiResponse = ApiModel.getInstance().getApiResponse(model, result);
            } catch (Exception e) {
                apiResponse = null;
            }
            return (T) apiResponse;
        } else {
            return null;
        }
    }


    /**
     * 获取connection
     *
     * @param paramUrl 请求接口地址
     * @return HttpURLConnection
     */
    private HttpURLConnection getConnection(String paramUrl) {
        HttpURLConnection connection = null;
        // 初始化connection
        try {
            // 根据地址创建URL对象
            URL url = new URL(SERVER_URL + paramUrl);
            // 根据URL对象打开链接
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            connection.setRequestMethod(REQUEST_MOTHOD);
            // 发送POST请求必须设置允许输入，默认为true
            connection.setDoInput(true);
            // 发送POST请求必须设置允许输出
            connection.setDoOutput(true);
            // 设置不使用缓存
            connection.setUseCaches(false);
            // 设置请求的超时时间
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Response-Type", "json");
            connection.setChunkedStreamingMode(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 拼接参数列表
     *
     * @param paramsMap 请求接口参数
     * @param jsonKey   返回数据类型
     * @return 拼接参数字符串
     */
    private String joinParams(Map<String, String> paramsMap, String jsonKey) {
        if (jsonKey.equals(RESPONSE_TYPE_JSON)) { //当请求类型为json时，将数据转为jsonobject。
            JSONObject object = new JSONObject();
            try {
                for (String key : paramsMap.keySet()) {
                    object.put(key, paramsMap.get(key));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonKey + "=" + object.toString();
        } else { //当请求数据不是json时，将数据拼接为字符串。
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : paramsMap.keySet()) {
                stringBuilder.append(key);
                stringBuilder.append("=");
                try {
                    stringBuilder.append(URLEncoder.encode(paramsMap.get(key), ENCODE_TYPE));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                stringBuilder.append("&");
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
    }
}
