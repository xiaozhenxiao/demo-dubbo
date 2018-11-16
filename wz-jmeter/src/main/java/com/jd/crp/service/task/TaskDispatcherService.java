package com.jd.crp.service.task;

import com.jd.crp.domain.task.Task;

/**
 * Created by quhailong on 2018/5/16.
 */
public interface TaskDispatcherService {

    /**
     * 分发任务
     * @param task
     */
    void dispatchMessage(Task task);
}
