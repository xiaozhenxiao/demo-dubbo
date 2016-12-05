package com.jd.disruptor.java8;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.jd.disruptor.demo.LongEvent;

import java.nio.ByteBuffer;

/**
 * Created by wangzhen23 on 2016/11/17.
 */
public class LongEventProducerWithTranslator {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = (LongEvent event, long sequence, ByteBuffer bb)-> event.setValue(bb.getLong(0));

    public void onData(ByteBuffer bb)
    {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }
}
