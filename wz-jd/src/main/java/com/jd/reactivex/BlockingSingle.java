package com.jd.reactivex;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.internal.operators.BlockingOperatorToFuture;
import rx.internal.util.BlockingUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by wangzhen23 on 2016/12/2.
 */
public final class BlockingSingle<T> {
    private final Single<? extends T> single;

    private BlockingSingle(Single<? extends T> single) {
        this.single = single;
    }

    /**
     * Converts a {@link Single} into a {@code BlockingSingle}.
     *
     * @param <T> the value type of the sequence
     * @param single the {@link Single} you want to convert
     * @return a {@code BlockingSingle} version of {@code single}
     */
    public static <T> BlockingSingle<T> from(Single<? extends T> single) {
        return new BlockingSingle<T>(single);
    }

    /**
     * Returns the item emitted by this {@code BlockingSingle}.
     * <p/>
     * If the underlying {@link Single} returns successfully, the value emitted
     * by the {@link Single} is returned. If the {@link Single} emits an error,
     * the throwable emitted ({@link SingleSubscriber#onError(Throwable)}) is
     * thrown.
     *
     * @return the value emitted by this {@code BlockingSingle}
     */
    public T value() {
        final AtomicReference<T> returnItem = new AtomicReference<T>();
        final AtomicReference<Throwable> returnException = new AtomicReference<Throwable>();
        final CountDownLatch latch = new CountDownLatch(1);
        Subscription subscription = single.subscribe(new SingleSubscriber<T>() {
            @Override
            public void onSuccess(T value) {
                returnItem.set(value);
                latch.countDown();
            }

            @Override
            public void onError(Throwable error) {
                returnException.set(error);
                latch.countDown();
            }
        });

        BlockingUtils.awaitForComplete(latch, subscription);
        Throwable throwable = returnException.get();
        if (throwable != null) {
            throw Exceptions.propagate(throwable);
        }
        return returnItem.get();
    }

    /**
     * Returns a {@link Future} representing the value emitted by this {@code BlockingSingle}.
     *
     * @return a {@link Future} that returns the value
     */
    @SuppressWarnings("unchecked")
    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(((Single<T>)single).toObservable());
    }
}
