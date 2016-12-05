package com.jd.reactivex;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by wangzhen23 on 2016/12/3.
 */
public class ObservableDemo {

    public static void main(String[] args) {
        String[] strArray = {"" , ""};
        Observable<String> o = Observable.from(strArray);

        Integer[] list = {5, 6, 7, 8};
        Observable<Integer> oo = Observable.from(list);

        Observable<String> ooo = Observable.just("one object");

        Observable.OnSubscribe<String> onSubscriber1 = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onCompleted();
            }
        };
        Subscriber<String> subscriber1 = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted!");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext: " + s);
            }
        };

        Subscriber<Integer> subscriber2 = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("================================onCompleted!");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer s) {
                System.out.println("=============================onNext: " + s);
            }
        };

        /**1、直接调用  **/
        Observable.create(onSubscriber1).subscribe(subscriber1);
        /** 2、lift  **/
        Func1<String, Integer> transformer1 = new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return Integer.parseInt(s);
            }
        };
//        Observable.create(onSubscriber1).map(transformer1).subscribe(subscriber2);
        Observable.create(onSubscriber1).map(s -> Integer.parseInt(s)+10).subscribe(subscriber2);
    }
}
