package com.jd.quartz;

import com.jd.quartz.job.SimpleJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * SimpleTrigger Main
 * wangzhen23
 * 2017/8/21.
 */
public class SimpleTriggerRunner {
    public static void main(String[] args) {
        try {
            JobDetail jobDetail = newJob(SimpleJob.class).withIdentity("myJob", "group1")
                    .usingJobData("jobSays", "Hello World!")
                    .usingJobData("myFloatValue", 3.141f)
                    .build();

            Trigger simpleTrigger = newTrigger().withIdentity("simpleTrigger", "group1").
                    withSchedule(simpleSchedule().withIntervalInSeconds(2).withRepeatCount(100)).build();

//            Trigger cronTrigger = newTrigger().withIdentity("cornTrigger", "group2")
//                    .withSchedule(cronSchedule("0/2 * * * * ?"))
//                    .endAt(futureDate(10, DateBuilder.IntervalUnit.SECOND)).build();

            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            scheduler.scheduleJob(jobDetail, simpleTrigger);
            scheduler.start();

            Thread.sleep(11000);
            System.out.println("===============END=============================");
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
