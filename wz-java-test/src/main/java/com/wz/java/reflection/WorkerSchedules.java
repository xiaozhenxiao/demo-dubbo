package com.wz.java.reflection;

import java.lang.annotation.*;

/**
 * Created by wangzhen23 on 2016/12/15.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WorkerSchedules {
    WorkerScheduled[] value();
}