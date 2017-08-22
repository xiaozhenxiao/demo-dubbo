package com.jd.quartz.job;

import org.quartz.*;

/**
 * quartz简单job
 * wangzhen23
 * 2017/8/21.
 */
public class SimpleJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobSays = dataMap.getString("jobSays");
        float myFloatValue = dataMap.getFloat("myFloatValue");
        System.out.println(context.getTrigger() + "Instance " + key + " of DumbJob says: " + jobSays + ", and val is: " + myFloatValue);
    }
}
