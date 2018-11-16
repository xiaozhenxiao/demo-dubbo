package com.wz.jd.spring;

import com.jd.crp.domain.task.Task;
import com.jd.crp.service.task.TaskDispatcherService;

/**
 * Created by wangzhen on 2016-06-16.
 */
public class ConsumerMain {
    public static void main(String[] args) {
        TaskDispatcherService ds = ApplicationContextUtil.getSpringBean("taskDispatcherService", TaskDispatcherService.class);

        Task task = new Task.Builder().id(166666L).type(222).build();
        ds.dispatchMessage(task);
        System.out.println("================END============");
    }
}
