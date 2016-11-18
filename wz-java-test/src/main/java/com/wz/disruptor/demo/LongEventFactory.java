package com.wz.disruptor.demo;

import com.lmax.disruptor.EventFactory;

/**
 * Created by wangzhen23 on 2016/11/17.
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
