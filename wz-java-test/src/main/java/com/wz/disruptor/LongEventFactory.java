package com.wz.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * TODO
 * wangzhen23
 * 2018/1/3.
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
