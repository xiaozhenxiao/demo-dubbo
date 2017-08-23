package com.jd.tbschedule.job;

import com.alibaba.fastjson.JSON;
import com.jd.tbschedule.domain.TaskModel;
import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 任务处理类
 * wangzhen23
 * 2017/8/22.
 */
@Component
public class SingleJob implements IScheduleTaskDealSingle<TaskModel>{
    private static final Logger LOG = LoggerFactory.getLogger(SingleJob.class);
    @Override
    public boolean execute(TaskModel taskModel, String ownSign) throws Exception {
        LOG.info("IScheduleTaskDealSingleTest执行开始啦.........." + new Date());
        System.out.println(JSON.toJSONString(taskModel));
        return true;
    }

    /**
     * @param taskParameter 自定义参数(字符串)
     * @param ownSign 环境
     * @param taskQueueNum 任务项的数量
     * @param taskItemList 集合中TaskItemDefine的id值对应任务项值
     * @param eachFetchDataNum 每次获取数据量
     * @return
     * @throws Exception
     */
    @Override
    public List<TaskModel> selectTasks(String taskParameter, String ownSign, int taskQueueNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        LOG.info("IScheduleTaskDealSingleTest配置的参数，taskParameter:{}，ownSina:{}，taskQueueNum:{},taskItemList:{}, eachFetchDataNum:{}", taskParameter, ownSign, taskQueueNum, taskItemList, eachFetchDataNum);

        LOG.info("IScheduleTaskDealSingleTest选择任务列表开始啦..........");
        List<TaskModel> models = new ArrayList<TaskModel>();
        models.add(new TaskModel(String.valueOf(System.currentTimeMillis()), "taosirTest1"));
        models.add(new TaskModel(String.valueOf(System.currentTimeMillis()), "taosirTest2"));

        return models;
    }

    @Override
    public Comparator<TaskModel> getComparator() {
        return null;
    }
}
