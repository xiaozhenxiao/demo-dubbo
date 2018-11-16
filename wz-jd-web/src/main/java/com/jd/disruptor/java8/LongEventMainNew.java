package com.jd.disruptor.java8;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.jd.disruptor.demo.LongEvent;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wangzhen23 on 2016/11/17.
 */
public class LongEventMainNew {
    public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println(Thread.currentThread().getName() + "> sequence:" + sequence + " Event: " + event + " endOfBatch:" + endOfBatch);
    }

    public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
        event.setValue(buffer.getLong(0));
    }

    public static void main(String[] args) throws Exception {
        // Executor that will be used to construct new threads for consumers
        Executor executor = Executors.newCachedThreadPool();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, executor);

        // Connect the handler
        disruptor.handleEventsWith(LongEventMainNew::handleEvent);
        disruptor.handleEventsWithWorkerPool(val -> System.out.println("=========" + val));

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent(LongEventMainNew::translate, bb);
            Thread.sleep(1000);
        }
    }
}
