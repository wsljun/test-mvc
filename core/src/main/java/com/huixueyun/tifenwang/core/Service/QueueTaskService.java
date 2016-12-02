package com.huixueyun.tifenwang.core.Service;

import android.app.IntentService;
import android.content.Intent;

import com.huixueyun.tifenwang.api.ApiResponse;
import com.huixueyun.tifenwang.api.api.QueueApi;
import com.huixueyun.tifenwang.api.api.QueueApiImpl;
import com.huixueyun.tifenwang.core.Broadcast.BroadcastActionParameter;
import com.huixueyun.tifenwang.model.model.BehaviorInfo;
import com.huixueyun.tifenwang.model.utils.Constants;
import com.huixueyun.tifenwang.model.utils.L;

import java.util.HashMap;

public class QueueTaskService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    private static final String name = "com.huixueyun.tifenwang.core.Service.QueueTaskService";
    //QueueApi对象
    private QueueApi queueApi;

    /**
     * 构造函数 初始化QueueApiImpl实现类
     */
    public QueueTaskService() {
        super(name);
        this.queueApi = new QueueApiImpl();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String type = "";
        try {
            type = intent.getStringExtra("type");
        } catch (Exception e) {
            L.e("获取队列任务的type发生异常！");
        }

        if (type.equals(Constants.BEHAVIOR)) {
            BehaviorInfo info = (BehaviorInfo) intent.getSerializableExtra("params");
            HashMap<String, String> paramMap = info.getBehavior();
            queueApi.sendBehavior(paramMap);
        }
    }

    /**
     * 通过发广播的形式将队列任务参数返回给请求对象
     *
     * @param apiResponse 需要返回给请求对象的参数
     * @param action      接受该广播的action
     */
    public void sendBroadcast(ApiResponse apiResponse, String action) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        BroadcastActionParameter parameter = new BroadcastActionParameter();
        parameter.setApiResponse(apiResponse);
        broadcastIntent.putExtra("params", parameter);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
