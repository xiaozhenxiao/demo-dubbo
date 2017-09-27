package com.jd.jvm.referemce;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * wangzhen23
 * 2017/9/26.
 */
public class ReferenceTest {
    public static void main(String[] args) {
//        softReference();
        weakReference();
    }

    private static void weakReference() {
        WeakReference<MyObject> wrakRef = null;

        MyObject obj = new MyObject();
        ReferenceQueue<MyObject> weakQueue = new ReferenceQueue<>();
        wrakRef = new WeakReference<MyObject>(obj, weakQueue);

        new CheckRefQueue<WeakReference<Object>>(weakQueue).start();
        obj = null;
//        Object obj1 = new ArrayList<>();
        System.out.println("before GC:" + wrakRef.get());
        System.gc();
        System.out.println("after GC:" + wrakRef.get());
    }

    private static void softReference() {
        MyObject obj = new MyObject();
        ReferenceQueue<MyObject> softQueue = new ReferenceQueue<>();
        SoftReference<MyObject> softRef = new SoftReference<MyObject>(obj, softQueue);

        new CheckRefQueue<SoftReference<Object>>(softQueue).start();
        obj = null;
        System.gc();
        System.out.println("after GC:" + softRef.get());
        byte[] b = new byte[4 * 1024 * 869];
        System.out.println("memery after GC:" + softRef.get());
    }
}

class CheckRefQueue<T> extends Thread {
    ReferenceQueue<MyObject> softQueue;

    public CheckRefQueue(ReferenceQueue<MyObject> softQueue) {
        this.softQueue = softQueue;
    }

    @Override
    public void run() {
        T softRef = null;
        try {
            softRef = (T) softQueue.remove();
        } catch (Exception e) {
        }
        if (softRef != null) {
            System.out.println("Object for SoftReference is " + (softRef instanceof SoftReference ? ((SoftReference<MyObject>)softRef).get() : ((WeakReference<Object>) softRef).get()));
        }
    }
}
