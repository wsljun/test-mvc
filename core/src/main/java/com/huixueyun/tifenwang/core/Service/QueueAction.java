package com.huixueyun.tifenwang.core.Service;

import com.huixueyun.tifenwang.model.model.BehaviorInfo;

import java.util.HashMap;

/**
 * QueueAction接口
 *
 * @version 1.0
 * @date 16/3/3
 */
public interface QueueAction {
    /**
     * 发送行为
     *
     * @param info 行为统计
     */
    void sendBehavior(HashMap<String,String> info);
}
