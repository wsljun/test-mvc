package com.huixueyun.tifenwang.model.model;

import java.io.Serializable;
import java.util.HashMap;

public class BehaviorInfo implements Serializable {
    private HashMap<String, String> behavior = null; //行为统计数据

    public HashMap<String, String> getBehavior() {
        return behavior;
    }

    public void setBehavior(HashMap<String, String> behavior) {
        this.behavior = behavior;
    }
}