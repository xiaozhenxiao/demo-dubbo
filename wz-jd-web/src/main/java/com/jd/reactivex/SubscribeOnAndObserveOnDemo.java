package com.jd.reactivex;

import com.google.common.collect.Maps;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangzhen23 on 2016/12/5.
 */
public class SubscribeOnAndObserveOnDemo {
    private static Map<Integer, ExecutorService> threadPoolMap = Maps.newHashMap();

    public SubscribeOnAndObserveOnDemo() {
        initThreadPool();
    }

    private static void initThreadPool() {
        threadPoolMap.put(TaskType.RECHARGE.getCode(), Executors.newFixedThreadPool(5, r -> new Thread(r, TaskType.RECHARGE.getCode()+"")));
        threadPoolMap.put(TaskType.REFUND.getCode(), Executors.newFixedThreadPool(5, r -> new Thread(r, TaskType.REFUND.getCode()+"")));
        threadPoolMap.put(TaskType.JIESUAN.getCode(), Executors.newFixedThreadPool(5, r -> new Thread(r, TaskType.JIESUAN.getCode()+"")));
        threadPoolMap.put(TaskType.ORDERCENTER.getCode(), Executors.newFixedThreadPool(5, r -> new Thread(r, TaskType.ORDERCENTER.getCode()+"")));
    }

    public static void main(String[] args) {
//        subscribeOn();
//        observeOn();
        initThreadPool();
        /**  测试线程  **/
        for (int i = 0; i < 10000; i++) {
            dispatchMessage(1, i);
        }
    }

    public static void dispatchMessage(Integer taskType, Integer taskId){
        Observable
                .create((Observable.OnSubscribe<String>) subscriber -> subscriber.onNext("RxJava" + taskId))
                .subscribeOn(getSchedulerByType(TaskType.RECHARGE.getCode()))
                .subscribe(result -> System.out.println(Thread.currentThread().getName() + "========" + result));
    }
    public static void subscribeOn() {
        Observable
                .create((Observable.OnSubscribe<String>) subscriber -> {
                    threadInfo("OnSubscribe.call()");
                    subscriber.onNext("RxJava");
                })
                .subscribeOn(getNamedScheduler("create之后的subscribeOn"))
                .doOnSubscribe(() -> threadInfo(".doOnSubscribe()-1"))
                .subscribeOn(getNamedScheduler("doOnSubscribe1之后的subscribeOn"))
                .doOnSubscribe(() -> threadInfo(".doOnSubscribe()-2"))
                .subscribe(s -> {
                    threadInfo(".onNext()");
                    System.out.println(s + "-onNext");
                });
    }

    public static void observeOn() {
        Observable
                .create((Observable.OnSubscribe<String>) subscriber -> {
                    threadInfo("observeOn.call()");
                    subscriber.onNext("RxJava");
                })
                .observeOn(getNamedScheduler("map之前的observeOn"))
                .map(s -> {
                    threadInfo(".map()-1");
                    return s + "-map1";
                })
                .map(s -> {
                    threadInfo(".map()-2");
                    return s + "-map2";
                })
                .observeOn(getNamedScheduler("subscribe之前的observeOn"))
                .subscribe(s -> {
                    threadInfo(".onNext()");
                    System.out.println(s + "-onNext");
                });
    }

    public static Scheduler getSchedulerByType(Integer type) {
        return Schedulers.from(threadPoolMap.get(type));
        /** 这样每次都会生成新的线程池，导致内存线程暴增 **/
//        return Schedulers.from(Executors.newFixedThreadPool(5, r -> new Thread(r, TaskType.RECHARGE.getCode() + "_1")));
    }

    public static Scheduler getNamedScheduler(String name) {
        return Schedulers.from(Executors.newCachedThreadPool(r -> new Thread(r, name)));
    }

    public static void threadInfo(String caller) {
        System.out.println(caller + " => " + Thread.currentThread().getName());
    }

    enum TaskType {
        RECHARGE(1, "充值任务"), REFUND(2, "退款任务"), JIESUAN(3, "结算任务"), ORDERCENTER(4, "同步订单同步订单件任务");

        Integer code;
        String desc;

        TaskType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
