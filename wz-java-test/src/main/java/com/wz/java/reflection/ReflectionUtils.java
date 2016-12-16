package com.wz.java.reflection;

import com.alibaba.fastjson.JSON;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by wangzhen23 on 2016/12/15.
 */
public class ReflectionUtils {
    public static void main(String[] args) {
        Son son = new Son();
        Class<?> targetClass = AopUtils.getTargetClass(son);
        org.springframework.util.ReflectionUtils.doWithMethods(targetClass, new org.springframework.util.ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                for (WorkerScheduled scheduled :
                        AnnotationUtils.getRepeatableAnnotations(method, WorkerScheduled.class, WorkerSchedules.class)) {
                    System.out.println(scheduled.scheduleName());
                }
            }
        });
        Method[] methods = targetClass.getMethods();
        Annotation[] declareAnnotations = methods[0].getDeclaredAnnotations();
        for (Annotation declareAnnotation : declareAnnotations) {
            System.out.println("=================declare=====================");
            System.out.println(JSON.toJSONString(declareAnnotation));
            System.out.println("=================declare=====================");
        }
        Annotation[] annotations = methods[0].getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("-------------annotations---------------------");
            System.out.println(JSON.toJSONString(annotation));
            System.out.println("-------------annotations---------------------");
        }

    }

    static class Father {
        @WorkerScheduled(
                cron = "0 0/10 * * * ? ",
                scheduleName = "father",
                desc = "task14.desc.refund",
                autoStartup = false,
                startupDelay = 20)
        public String getName() {
            System.out.println("Father's name is father ");
            return "father";
        }
    }

    static class Son extends Father {
        @WorkerScheduled(
                cron = "0 0/5 * * * ? ",
                scheduleName = "son",
                desc = "task14.desc.refund",
                autoStartup = false,
                startupDelay = 20)
        public String getName() {
            System.out.println("Son's name is son" + " " + this.getClass());
            return "son";
        }
    }
}
