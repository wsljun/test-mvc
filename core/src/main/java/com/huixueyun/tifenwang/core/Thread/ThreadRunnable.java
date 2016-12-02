package com.huixueyun.tifenwang.core.Thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.huixueyun.tifenwang.api.ApiResponse;
import com.huixueyun.tifenwang.api.api.AccountApi;
import com.huixueyun.tifenwang.core.Action.AccountActionImpl;
import com.huixueyun.tifenwang.core.ActionCallbackListener;
import com.huixueyun.tifenwang.model.utils.Constants;
import com.huixueyun.tifenwang.model.utils.L;

import java.util.HashMap;

public class ThreadRunnable implements Runnable {
    //cancle标志
    private boolean cancleTask = false;
    //ActionCallbackListener对象
    private ActionCallbackListener listener;
    //请求类型
    private String type;
    //上下文
    private Context context;
    //请求数据
    private HashMap<String, String> param;

    private Handler handler = new Handler() {
        /**
         * Overriding methods
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (cancleTask == false) {
                ApiResponse response = (ApiResponse) msg.obj;
                if (response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFailure(response.getEvent(), response.getMsg());
                    }
                } else {
                    listener.onFailure("CONNECT_FAIL", "请求失败");
                }
            } else {
                L.e("任务" + type + "在执行后被取消");
            }
        }

    };


    /**
     * 构造函数
     *
     * @param context
     */
    public ThreadRunnable(Context context) {
        this(context, "", null, null);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param type
     * @param param
     * @param listener
     */
    public ThreadRunnable(Context context, String type, HashMap<String, String> param, ActionCallbackListener listener) {
        this.context = context;
        this.listener = listener;
        this.type = type;
        this.param = param;
    }

    /**
     * 获取AccountApi对象
     *
     * @return
     */
    private AccountApi getAccountApi() {
        return AccountActionImpl.getAppActionImplInstance(context).getAccountApi();
    }

    @Override
    public void run() {
        if (cancleTask == false) {
            running();
        } else {
            L.e("任务" + type + "在执行前被取消");
        }
    }

    /**
     * 任务执行
     */
    private void running() {
        try {
            Message message = new Message();
            if (type.equals(Constants.LOGIN))
                message.obj = getAccountApi().login(param);
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Runnable类型
     *
     * @return Runnable类型
     */
    public String getRunnableType() {
        return type;
    }

    /**
     * 设置cancle标志
     *
     * @param cancleTask
     */
    public void setCancleTaskUnit(boolean cancleTask) {
        this.cancleTask = cancleTask;
    }
}
