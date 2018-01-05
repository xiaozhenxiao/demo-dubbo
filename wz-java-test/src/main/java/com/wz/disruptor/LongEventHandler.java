package com.wz.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * consumer
 * wangzhen23
 * 2018/1/4.
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println(Thread.currentThread().getName() + " = " + sequence + " - " + endOfBatch + " - " + "Event: " + event);
    }
}
