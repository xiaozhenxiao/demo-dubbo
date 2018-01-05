package com.wz.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * TODO
 * wangzhen23
 * 2018/1/3.
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    private int i = 0;
    @Override
    public LongEvent newInstance() {
        System.out.println(Thread.currentThread().getName() + " @ " + i++);
        return new LongEvent();
    }
}
