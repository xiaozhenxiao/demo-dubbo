package com.wz.ttl.trace;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DistributedTracer(DT) use demo.
 * wangzhen23
 * 2019/7/15.
 */
public class DistributedTracer {
    private static ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));

    private static ConcurrentHashMap<String, LeafSpanIdInfo> traceId2LeafSpanIdInfo = new ConcurrentHashMap<>();

    private static TransmittableThreadLocal<DtTransferInfo> transferInfo = new TransmittableThreadLocal<DtTransferInfo>() {
        /*
        @Override
        protected DtTransferInfo childValue(DtTransferInfo parentValue) {
            // **注意**：
            // 新建线程时，从父线程继承值时，计数加1
            // 对应线程结束时，没有回调以清理ThreadLocal中的Context！，Bug！！
            // InheritableThreadLocal 没有提供 对应的拦截方法。。。 计数不配对了。。。
            // 但是一个线程就一个Context没清，线程数有限，Context占用内存一般很小，可以接受。
            increaseSpanIdRefCount();
            return super.childValue(parentValue);
        }
        */

        @Override
        protected void beforeExecute() {
            super.beforeExecute();
            increaseSpanIdRefCount();
        }

        @Override
        protected void afterExecute() {
            decreaseSpanIdRefCount();
        }
    };

    public static void main(String[] args) throws InterruptedException {
        rpcInvokeIn();

        TimeUnit.MILLISECONDS.sleep(100);
    }

    private static void rpcInvokeIn() {
        ////////////////////////////////////////////////
        // DistributedTracer Framework Code
        ////////////////////////////////////////////////

        // Get Trace Id and Span Id from RPC Context
        String traceId = "traceId_XXXYYY";
        String baseSpanId = "1.1";
        transferInfo.set(new DtTransferInfo(traceId, baseSpanId));
        traceId2LeafSpanIdInfo.put(traceId, new LeafSpanIdInfo());

        increaseSpanIdRefCount();

        ////////////////////////////////////////////////
        // Biz Code
        ////////////////////////////////////////////////
        syncMethod();

        ////////////////////////////////////////////////
        // DistributedTracer Framework Code
        ////////////////////////////////////////////////
        decreaseSpanIdRefCount();
    }



    private static void syncMethod() {
        // async call by TTL Executor, Test OK!
        executorService.submit(()-> asyncMethod());

        // async call by new Thread
        // FIXME Bug!! 没有 Increase/Decrease reference counter操作!
        new Thread("Thread-by-new"){
            @Override
            public void run(){
                syncMethod_ByNewThread();
            }
        }.start();

        invokeServerWithRpc("server 1");
    }

    private static void asyncMethod() {
        invokeServerWithRpc("server 2");
    }

    private static void syncMethod_ByNewThread() {
        invokeServerWithRpc("server 3");
    }


    // RPC invoke
    private static void invokeServerWithRpc(String server) {
        ////////////////////////////////////////////////
        // DistributedTracer Framework Code
        ////////////////////////////////////////////////

        Integer leafSpanCurrent = increaseLeafSpanCurrentAndReturn();

        // Set RpcContext
        // Mocked, should use RPC util to get Rpc Context instead
        ConcurrentHashMap<String, String> rpcContext = new ConcurrentHashMap<>(2);

        rpcContext.put("traceId", transferInfo.get().traceId);
        rpcContext.put("spanId", transferInfo.get().baseSpanId + "." + leafSpanCurrent);

        // Do Rpc
        // ...
        System.out.printf("Do Rpc invocation to server %s with %s%n", server, rpcContext);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////

    static class DtTransferInfo {
        String traceId;
        String baseSpanId;

        public DtTransferInfo(String traceId, String baseSpanId) {
            this.traceId = traceId;
            this.baseSpanId = baseSpanId;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getBaseSpanId() {
            return baseSpanId;
        }

        public void setBaseSpanId(String baseSpanId) {
            this.baseSpanId = baseSpanId;
        }
    }

    static class LeafSpanIdInfo {
        AtomicInteger current = new AtomicInteger(1);

        AtomicInteger refCounter = new AtomicInteger(0);
    }


    private static void increaseSpanIdRefCount() {
        String traceId = transferInfo.get().getTraceId();
        int refCounter = traceId2LeafSpanIdInfo.get(traceId).refCounter.incrementAndGet();

        System.out.printf("DEBUG: Increase reference counter(%s) for traceId %s in thread %s%n", refCounter, traceId, Thread.currentThread().getName());
    }

    private static void decreaseSpanIdRefCount() {
        String traceId = transferInfo.get().getTraceId();
        LeafSpanIdInfo leafSpanIdInfo = traceId2LeafSpanIdInfo.get(traceId);

        int refCounter = leafSpanIdInfo.refCounter.decrementAndGet();
        System.out.printf("DEBUG: Decrease reference counter(%s) for traceId %s in thread %s%n", refCounter, traceId, Thread.currentThread().getName());

        if (refCounter == 0) {
            traceId2LeafSpanIdInfo.remove(traceId);

            System.out.printf("DEBUG: Clear traceId2LeafSpanIdInfo for traceId %s in thread %s%n", traceId, Thread.currentThread().getName());
        } else if (refCounter < 0) {
            throw new IllegalStateException("Leaf Span Id Info Reference counter has Bug!!");
        }
    }

    private static Integer increaseLeafSpanCurrentAndReturn() {
        String traceId = transferInfo.get().getTraceId();
        return traceId2LeafSpanIdInfo.get(traceId).current.getAndIncrement();
    }
}
