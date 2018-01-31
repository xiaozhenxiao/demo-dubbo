package com.wz.common.pool;

/**
 * TODO
 * wangzhen23
 * 2018/1/31.
 */
public class ConnectionTest {
    private String key;

    public ConnectionTest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ConnectionTest:" + this.key;
    }
}
