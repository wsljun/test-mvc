package com.huixueyun.tifenwang.core.Service;

import android.content.Context;
import android.content.Intent;

import com.huixueyun.tifenwang.model.model.BehaviorInfo;
import com.huixueyun.tifenwang.model.utils.Constants;

import java.util.HashMap;

/**
 * QueueActionImpl接口的实现类
 *
 * @version 1.0
 * @date 16/3/3
 */
public class QueueActionImpl implements QueueAction {
    //上下文
    private Context context;

    /**
     * 构造函数
     *
     * @param context
     */
    public QueueActionImpl(Context context) {
        this.context = context;
    }

    @Override
    public void sendBehavior(HashMap<String, String> paramMap) {
        Intent intent = new Intent();
        intent.setClass(context, QueueTaskService.class);
        BehaviorInfo behaviorInfo = new BehaviorInfo();
        behaviorInfo.setBehavior(paramMap);
        intent.putExtra("params", behaviorInfo);
        intent.putExtra("type", Constants.BEHAVIOR);
        context.startService(intent);
    }
}
