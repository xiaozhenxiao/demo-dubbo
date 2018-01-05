package com.wz.disruptor;

/**
 * TODO
 * wangzhen23
 * 2018/1/3.
 */
public class LongEvent {
    private Long value;

    public void set(long value) {
        this.value = value;
    }

    void clear() {
        value = null;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
