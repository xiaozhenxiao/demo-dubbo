package com.wz.rx;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

import static javafx.scene.input.KeyCode.R;

/**
 * RxJava
 * wangzhen23
 * 2018/1/11.
 */
public class RxDemo {
    private static final Logger LOG = LoggerFactory.getLogger(RxDemo.class);

    public static void main(String[] args) {
//        hello("Ben", "George");
        for (int i = 0; i < 10; i++) {
            create("xiao " + i);
        }
    }

    public static void hello(String... names) {
        Observable.from(names).subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                System.out.println("Hello " + s + "!");
            }

        });
    }

    public static void create(String message) {
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            System.out.println(Thread.currentThread().getName() + " fire " + message);
            // 把Drawable对象发送出去
            subscriber.onNext(message);
            subscriber.onCompleted();

        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LOG.info(e.toString());
                    }

                    @Override
                    public void onNext(String drawable) {
                        System.out.println(Thread.currentThread().getName() + " recive:" + drawable);
                    }
                });
    }
}
