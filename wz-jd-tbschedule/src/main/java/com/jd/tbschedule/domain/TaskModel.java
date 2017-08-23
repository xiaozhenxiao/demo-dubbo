package com.jd.tbschedule.domain;

/**
 * 任务模型
 * wangzhen23
 * 2017/8/22.
 */
public class TaskModel {
    private String timeStamp;
    private String type;

    public TaskModel(String time, String type) {
        this.timeStamp = time;
        this.type = type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
