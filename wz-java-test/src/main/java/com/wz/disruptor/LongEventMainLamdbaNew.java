package com.wz.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * TODO
 * wangzhen23
 * 2018/1/4.
 */
public class LongEventMainLamdbaNew {
    public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
        try {
            System.out.println(Thread.currentThread().getName() + " = " + sequence + " - " + endOfBatch + " - " + "Event: " + event);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
        event.set(buffer.getLong(0));
    }

    public static void main(String[] args) throws Exception {
        // Executor that will be used to construct new threads for consumers
        Executor executor = Executors.newCachedThreadPool();
        //替换executor
        ThreadFactory myThreadFactory = new MyThreadFactory("xiao");

        // The factory for the event
        LongEventFactory factory = new LongEventFactory();
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
//        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, myThreadFactory);
        //调优参数
//        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, myThreadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, myThreadFactory, ProducerType.MULTI, new BlockingWaitStrategy());

        // Connect the handler
        /*************************************************************************************************************************************************************/
        /** ①,  ①和②可以并行执行**/
        /*disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName() + " ++ " + sequence + " - " + endOfBatch + " - " + "Event: " + event);
        });*/
        /** ②, ⑴、⑵和⑶按顺序执行**/
        disruptor.handleEventsWith(LongEventMainLamdbaNew::handleEvent) //⑴
                //⑵
                .handleEventsWith((event, sequence, endOfBatch) -> System.out.println(Thread.currentThread().getName() + " * " + sequence + " - " + endOfBatch + " - " + "Event: " + event))
                //⑶
                .then((event, sequence, endOfBatch) -> {
                    System.out.println(Thread.currentThread().getName() + " # " +  + sequence + " - " + endOfBatch + " - " + "Event: " + event);
                    event.clear();
                });
        /*************************************************************************************************************************************************************/
        /**+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++**/
        /**①和②并行执行，③在①和②之后执行**/
        /*disruptor.handleEventsWith(LongEventMainLamdbaNew::handleEvent) *//**①**//*
                .and(disruptor.handleEventsWith((event, sequence, endOfBatch) -> { *//**②**//*
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + " ++ " + sequence + " - " + endOfBatch + " - " + "Event: " + event);
                }).then((event, sequence, endOfBatch) -> { *//**③**//*
                            System.out.println(Thread.currentThread().getName() + " # " +  + sequence + " - " + endOfBatch + " - " + "Event: " + event);
                            event.clear();
                        }));*/
        /**+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++**/

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        /*ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent(LongEventMainLamdbaNew::translate, bb);
            Thread.sleep(1000);
        }*/

        /**多个生产者**/
        for (int i = 0; i < 5; i++) {
            new Thread(new ProducerThread(ringBuffer)).start();
        }
        while (true) {
            Thread.sleep(10000);
        }
    }
}
