package com.wz.java.lock;

import java.util.List;

/**
 * 锁池:假设线程A已经拥有了某个对象(注意:不是类)的锁，而其它的线程想要调用这个对象的某个synchronized方法(或者synchronized块)，
 * 由于这些线程在进入对象的synchronized方法之前必须先获得该对象的锁的拥有权，但是该对象的锁目前正被线程A拥有，所以这些线程就进入了该对象的锁池中。
 * 等待池:假设一个线程A调用了某个对象的wait()方法，线程A就会释放该对象的锁后，进入到了该对象的等待池中。
 * notifyAll调用后，会将全部线程由等待池移到锁池，然后参与锁的竞争，竞争成功则继续执行，如果不成功则留在锁池等待锁被释放后再次参与竞争。
 * notify调用后，只会将等待池中的一个随机线程移到锁池。
 * wangzhen23
 * 2017/8/25.
 */
public class Consumer implements Runnable{
    List<Integer> cache;

    public Consumer(List<Integer> cache) {
        this.cache = cache;
    }

    @Override
    public void run() {
        while (true) {
            consume();
        }
    }

    private void consume() {
        synchronized (cache) {
            try {
                while (cache.isEmpty()) {
                    cache.wait();
                }

                System.out.println("Consumer consumed [" + cache.remove(0) + "]");
                cache.notify();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
