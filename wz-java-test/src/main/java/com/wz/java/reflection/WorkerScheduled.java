package com.wz.java.reflection;

import java.lang.annotation.*;

/**
 * Created by wangzhen23 on 2016/12/15.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(value = WorkerSchedules.class)
public @interface WorkerScheduled {

    /**
     * Cron string.
     *
     * @return the string
     */
    String cron() default "";


    /**
     * Schedule prefix string.
     *
     * @return the string
     */
    String scheduleName() default "";

    /**
     * Auto startup boolean.
     *
     * @return the boolean
     */
    boolean autoStartup() default false ;

    /**
     * Startup delay int.
     *
     * @return the int
     */
    int startupDelay() default 0 ;

    /**
     * Thread count int.
     *
     * @return the int
     */
    int threadCount() default 1 ;

    /**
     * Job concurrent boolean.
     *
     * @return the boolean
     */
    boolean jobConcurrent() default false ;

    /**
     * desc
     * @return
     */
    String desc() default "nameless";

}
