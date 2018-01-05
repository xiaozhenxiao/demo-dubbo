package com.wz.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * wangzhen23
 * 2018/1/5.
 */
public class ProducerThread implements Runnable{

    RingBuffer<LongEvent> ringBuffer;

    public ProducerThread(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void run() {
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; l < 1024; l++) {
            bb.putLong(0, l);
            System.out.println(Thread.currentThread().getName() + " 生成消息:" + bb.getLong(0));
            ringBuffer.publishEvent(LongEventMainLamdbaNew::translate, bb);
        }
    }
}
