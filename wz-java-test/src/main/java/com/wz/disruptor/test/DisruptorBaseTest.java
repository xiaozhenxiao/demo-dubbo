package com.wz.disruptor.test;

import com.lmax.disruptor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lmax.disruptor.RingBuffer.createSingleProducer;

/**
 * TODO
 * wangzhen23
 * 2018/1/4.
 */
public class DisruptorBaseTest {
    protected static final Logger log = LoggerFactory.getLogger(DisruptorBaseTest.class);

    private static final int THREAD_NUMS = 2;
    private static final int BUFFER_SIZE = 1024 * 8;
    private static final long NUMS = 10L;

    public static void main(String[] args) throws InterruptedException {
        RingBuffer<MessageEvent> ringBuffer = createSingleProducer(
                MessageEvent.EVENT_FACTORY, BUFFER_SIZE,
                new YieldingWaitStrategy());
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_NUMS);
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        MessageMutationEventHandler[] handlers = new MessageMutationEventHandler[THREAD_NUMS];
        BatchEventProcessor<?>[] batchEventProcessors = new BatchEventProcessor[THREAD_NUMS];

        for (int i = 0; i < THREAD_NUMS; i++) {
            handlers[i] = new MessageMutationEventHandler();
            batchEventProcessors[i] = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, handlers[i]);
            ringBuffer.addGatingSequences(batchEventProcessors[i].getSequence());
        }

        CountDownLatch latch = new CountDownLatch(THREAD_NUMS);
        for (int i = 0; i < THREAD_NUMS; i++) {
            long n = batchEventProcessors[i].getSequence().get() + NUMS;
            System.out.println(n + "    " + NUMS + "  " + batchEventProcessors[i].getSequence().get());
            handlers[i].reset(latch, n);
            executors.submit(batchEventProcessors[i]);
        }
        long start = System.currentTimeMillis();

        for (long i = 0; i < NUMS; i++) {
            long sequence = ringBuffer.next();
            ringBuffer.get(sequence).setValue(i);
            ringBuffer.publish(sequence);
        }

        latch.await();
        long opsPerSecond = (NUMS * 1000L) / (System.currentTimeMillis() - start);

        for (int i = 0; i < THREAD_NUMS; i++) {
            batchEventProcessors[i].halt();
            if ((NUMS - 1) == handlers[i].getValue()) {

            } else {
                log.error("error");
            }
        }
        executors.shutdown();
        log.info(String.format("Run %d, Disruptor=%,d ops/sec%n", 1, opsPerSecond));
    }

    public static final class MessageMutationEventHandler implements
            EventHandler<MessageEvent> {
        private Long value = 0L;
        private long count;
        private CountDownLatch latch;

        public MessageMutationEventHandler() {

        }

        public long getValue() {
            return value;
        }

        public void reset(final CountDownLatch latch, final long expectedCount) {
            value = 0L;
            this.latch = latch;
            count = expectedCount;
        }

        @Override
        public void onEvent(final MessageEvent event, final long sequence,
                            final boolean endOfBatch) {
            System.out.println(Thread.currentThread().getName() + " onEvent:{" + event.getValue() + "}");
            value = event.getValue();
            if (count == sequence) {
                latch.countDown();
            }
        }
    }

    public static final class MessageEvent {
        private long value;

        public long getValue() {
            return value;
        }

        public void setValue(final long value) {
            this.value = value;
        }

        public final static EventFactory<MessageEvent> EVENT_FACTORY = new EventFactory<MessageEvent>() {
            public MessageEvent newInstance() {
                return new MessageEvent();
            }
        };
    }
}
