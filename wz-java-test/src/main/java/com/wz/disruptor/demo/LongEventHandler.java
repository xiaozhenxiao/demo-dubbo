package com.wz.disruptor.demo;

import com.lmax.disruptor.EventHandler;

/**
 * Created by wangzhen23 on 2016/11/17.
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(Thread.currentThread().getName() + "》demo/LongEventHandler > sequence:" + sequence + " Event: " + event + " endOfBatch:" + endOfBatch);
    }
}
