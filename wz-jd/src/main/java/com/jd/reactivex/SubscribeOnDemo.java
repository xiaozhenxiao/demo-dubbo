package com.jd.reactivex;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wangzhen23 on 2016/12/3.
 */
public class SubscribeOnDemo {
    public static void main(String[] args) {
        /**
         * new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
        subscriber.onNext("a");
        subscriber.onNext("b");

        subscriber.onCompleted();
        }
        }
         */
        /*Observable.create((Observable.OnSubscribe<String>)subscriber ->{
                        subscriber.onNext("a");
                        subscriber.onNext("b");
                        subscriber.onCompleted();
                    })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onNext(String integer) {
                        System.out.println(Thread.currentThread().getName() + " - " + integer);
                    }
                });*/

        mutilSubscribeOn();
    }

    public static void mutilSubscribeOn(){
        Observable.just("ss")
                .subscribeOn(Schedulers.newThread()) //----2----
//                .subscribeOn(Schedulers.io())   // ----1---
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(Thread.currentThread().getName() + " - " + s);
                    }
                });
    }
}
